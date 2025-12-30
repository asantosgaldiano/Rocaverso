package web.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import web.modelo.entities.Destacado;
import web.modelo.entities.Estado;
import web.modelo.entities.Evento;

public interface EventoRepository extends JpaRepository<Evento, Integer>{
	
	// Consultas personalizadas:
	List<Evento> findByDestacado(Destacado destacado);
	
	List<Evento> findByEstado(Estado estado);
	
	List<Evento> findByEstadoAndFechaInicioAfter(Estado estado, Date fechaInicio);


}
