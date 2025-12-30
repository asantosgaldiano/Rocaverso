package web.modelo.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.dto.EstadisticasUsuarioDto;
import web.modelo.entities.Dificultad;
import web.modelo.entities.TipoVia;
import web.modelo.entities.ViaRealizada;
import web.repository.ViaRealizadaRepository;

@Service
public class EstadisticasServiceImpl implements EstadisticasService {

    @Autowired
    private ViaRealizadaRepository viaRealizadaRepository;
	
	@Override
	public EstadisticasUsuarioDto obtenerEstadisticasUsuario(String email) {
		
        List<ViaRealizada> realizadas = viaRealizadaRepository.findByUsuarioEmail(email);
        int total = realizadas.size();

        if (total == 0) { // Esto evita hacer cálculos innecesarios y errores por listas vacías.
            return new EstadisticasUsuarioDto(0, null, null);
        }

        // Calcular el Nivel medio de dificultad
        double mediaOrdinal = realizadas.stream()
            .mapToInt(vr -> vr.getVia().getDificultad().ordinal()) // Como es un Enum devuelvo la posicion numerica para calcular la media.
            .average()
            .orElse(0.0); // Aseguro que si la lista estuviera vacía, la media sea 0.

        int indiceMedia = (int) Math.round(mediaOrdinal); // Redondeo
        
        // Aseguro que nunca superemos el índice máximo.
        Dificultad nivelMedio = Dificultad.values()[Math.min(indiceMedia, Dificultad.values().length - 1)]; 

        // Tipo de vía más frecuente
        TipoVia tipoMasFrecuente = realizadas.stream()
       
        	// Agrupo por tipo de vía (vr.getVia().getTipo()) y se cuenta cuántas veces aparece cada tipo.
            .collect(Collectors.groupingBy(vr -> vr.getVia().getTipo(), Collectors.counting())) 
            .entrySet() // Convierto el Map en un conjunto de entradas (Map.Entry), para poder ordenarlas o compararlas.
            // Cada entrada tiene key (tipo de vía) y value (conteo).
            .stream() // Convierto las entradas del Map en un stream para poder usar operaciones
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey) // Del Map.Entry que tiene el valor máximo, extraigo la clave, es decir, el tipo de vía.
            .orElse(null);
        
        return new EstadisticasUsuarioDto(total, nivelMedio, tipoMasFrecuente);
    
	}

}
