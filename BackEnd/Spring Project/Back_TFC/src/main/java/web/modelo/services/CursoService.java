package web.modelo.services;

import java.util.List;

import web.dto.PlanificacionCursoRequestDto;
import web.modelo.entities.Curso;
import web.modelo.entities.Estado;

public interface CursoService extends CrudGenerico<Curso, Integer> {

    // Consultas específicas
    List<Curso> buscarPorEstado(Estado estado);

    List<Curso> buscarPorActivos();

    List<Curso> buscarPorMonitor(String emailMonitor); 
    
	boolean tieneConflictosDeHorario(
			String emailMonitor, 
			int idCurso, 
			List<PlanificacionCursoRequestDto> planificacionesNuevas);
}
