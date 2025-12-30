package web.modelo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.dto.PlanificacionCursoRequestDto;
import web.modelo.entities.PlanificacionCurso;
import web.repository.PlanificacionCursoRepository;

@Service
public class PlanificacionCursoServiceImpl implements PlanificacionCursoService {

    @Autowired
    private PlanificacionCursoRepository planificacionCursoRepo;

    @Override
    public List<PlanificacionCurso> buscarTodos() {
        return planificacionCursoRepo.findAll();
    }

    @Override
    public PlanificacionCurso buscarUno(Integer id) {
        return planificacionCursoRepo.findById(id).orElse(null);
    }

    @Override
    public PlanificacionCurso insertUno(PlanificacionCurso entity) {
        try {
            return planificacionCursoRepo.save(entity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int updateUno(PlanificacionCurso entity) {
        if (planificacionCursoRepo.existsById(entity.getIdPlanificacion())) {
            try {
                planificacionCursoRepo.save(entity);
                return 1;
            } catch (Exception e) {
                return -1;
            }
        }
        return 0;
    }

    @Override
    public int deleteUno(Integer id) {
        if (planificacionCursoRepo.existsById(id)) {
            try {
                planificacionCursoRepo.deleteById(id);
                return 1;
            } catch (Exception e) {
                return -1;
            }
        }
        return 0;
    }

	@Override
	public List<PlanificacionCurso> buscarPorCurso(int idCurso) {
	    return planificacionCursoRepo.findByCursoIdCurso(idCurso);
	}


}
