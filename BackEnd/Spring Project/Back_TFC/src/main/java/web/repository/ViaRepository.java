package web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import web.modelo.entities.Dificultad;
import web.modelo.entities.EstadoVia;
import web.modelo.entities.TipoVia;
import web.modelo.entities.Via;
import web.modelo.entities.Zona;

public interface ViaRepository extends JpaRepository<Via, Integer> {
	
	// Consultas personalizadas:
    List<Via> findByTipo(TipoVia tipo);
    List<Via> findByDificultad(Dificultad dificultad);
    List<Via> findByEstado(EstadoVia estado);
    List<Via> findByUbicacion(Zona ubicacion);
    
}
