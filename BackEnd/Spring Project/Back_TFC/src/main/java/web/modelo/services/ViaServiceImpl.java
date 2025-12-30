package web.modelo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.modelo.entities.Dificultad;
import web.modelo.entities.EstadoVia;
import web.modelo.entities.TipoVia;
import web.modelo.entities.Via;
import web.modelo.entities.Zona;
import web.repository.ViaRepository;

@Service
public class ViaServiceImpl implements ViaService {

    @Autowired
    private ViaRepository viaRepo;
	
	@Override
    public Via buscarUno(Integer idVia) {
        return viaRepo.findById(idVia).orElse(null);
    }

	@Override
	public List<Via> buscarTodos() {
        return viaRepo.findAll();
	}

    @Override
    public Via insertUno(Via via) {
        try {
            if (viaRepo.existsById(via.getIdVia())) {
                return null; // No insertar si ya existe el id
            } else {
                return viaRepo.save(via);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateUno(Via via) {
        try {
            if (viaRepo.existsById(via.getIdVia())) {
                viaRepo.save(via);
                return 1; // Éxito
            } else {
                return 0; // No existe para actualizar
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    @Override
    public int deleteUno(Integer idVia) {
        try {
            if (viaRepo.existsById(idVia)) {
                viaRepo.deleteById(idVia);
                return 1; // Éxito
            } else {
                return 0; // No existe para borrar
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    @Override
    public List<Via> buscarPorTipo(TipoVia tipo) {
        return viaRepo.findByTipo(tipo);
    }

    @Override
    public List<Via> buscarPorDificultad(Dificultad dificultad) {
        return viaRepo.findByDificultad(dificultad);
    }

	@Override
	public List<Via> buscarPorEstado(EstadoVia estado) {
	    return viaRepo.findByEstado(estado);
	}

	@Override
	public List<Via> buscarPorUbicacion(Zona ubicacion) {
	    return viaRepo.findByUbicacion(ubicacion);
	}

	@Override
	public List<Via> buscarFiltrado(TipoVia tipo, Dificultad dificultad, Zona zona) {
		
	    List<Via> todas = viaRepo.findAll();
	    
	    // Filtro las vías según los criterios indicados
	    return todas.stream()
	        .filter(v -> v.getEstado() == EstadoVia.ACTIVA) // Solo Activas
	        .filter(v -> tipo == null || v.getTipo() == tipo) // Filtro por tipo (si indico)
	        .filter(v -> dificultad == null || v.getDificultad() == dificultad) // Filtro por dificultad (si indico)
	        .filter(v -> zona == null || v.getUbicacion() == zona) // Filtro por zona  (si indico)
	        .toList(); // Devuelvo la lista final filtrada
	}
}
