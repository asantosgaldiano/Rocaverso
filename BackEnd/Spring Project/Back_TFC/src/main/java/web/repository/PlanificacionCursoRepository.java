package web.repository;

import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import web.modelo.entities.DiaSemana;
import web.modelo.entities.PlanificacionCurso;

public interface PlanificacionCursoRepository extends JpaRepository<PlanificacionCurso, Integer> {
    
	// Consultas personalizadas:
	List<PlanificacionCurso> findByCursoIdCurso(int idCurso);
	
	// Devuelve las planificaciones del mismo monitor que tienen horarios que se cruzan con una nueva propuesta, para evitar conflictos.
    @Query("""
    SELECT p FROM PlanificacionCurso p
    WHERE p.curso.monitor.email = :emailMonitor
      AND p.curso.idCurso <> :idCurso
      AND p.diaSemana = :diaSemana
      AND (
        (p.horaInicio < :horaFinNueva AND p.horaFin > :horaInicioNueva)
      )
    """)
    List<PlanificacionCurso> findConflictosPlanificacion(
        @Param("emailMonitor") String emailMonitor,
        @Param("idCurso") int idCurso,
        @Param("diaSemana") DiaSemana diaSemana,
        @Param("horaInicioNueva") LocalTime horaInicioNueva,
        @Param("horaFinNueva") LocalTime horaFinNueva);
}

