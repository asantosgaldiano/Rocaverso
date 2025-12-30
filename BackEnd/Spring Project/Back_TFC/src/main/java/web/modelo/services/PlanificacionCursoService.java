package web.modelo.services;

import java.util.List;

import web.dto.PlanificacionCursoRequestDto;
import web.modelo.entities.PlanificacionCurso;

public interface PlanificacionCursoService extends CrudGenerico<PlanificacionCurso, Integer> {

	List<PlanificacionCurso> buscarPorCurso(int idCurso);

}
