package web.modelo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.dto.AforoActualDto;
import web.modelo.entities.Rocodromo;
import web.repository.AccesoRepository;
import web.repository.RocodromoRepository;

@Service
public class AforoServiceImpl implements AforoService{

    @Autowired
    private RocodromoRepository rocodromoRepository;

    @Autowired
    private AccesoRepository accesoRepository;
    
	@Override
	public AforoActualDto obtenerAforoActual() {
	    
		Rocodromo rocodromo = rocodromoRepository.findAll().stream().findFirst() // Busca el primer registro del rocódromo. // Ojo si se hacen mas rocodromos
	            .orElseThrow(() -> new RuntimeException("No se ha configurado el rocódromo"));

	    int dentro = accesoRepository.contarUsuariosDentro();
	    int maximo = rocodromo.getAforoMaximo();

	    // Calcular porcentaje
	    int porcentaje = maximo > 0 ? (dentro * 100) / maximo : 0;
	    
	    return AforoActualDto.builder()
	            .aforoMaximo(maximo)
	            .usuariosDentro(dentro)
	            .porcentajeOcupado(porcentaje)
	            .build();
    }
}

