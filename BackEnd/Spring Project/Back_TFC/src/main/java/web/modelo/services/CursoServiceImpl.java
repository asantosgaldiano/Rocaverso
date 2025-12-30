package web.modelo.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.dto.PlanificacionCursoRequestDto;
import web.modelo.entities.Curso;
import web.modelo.entities.Estado;
import web.modelo.entities.PlanificacionCurso;
import web.repository.CursoRepository;
import web.repository.PlanificacionCursoRepository;


@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepo;
    
    @Autowired
    private PlanificacionCursoRepository planiRepo;
    
    @Override
    public Curso buscarUno(Integer idCurso) {
        return cursoRepo.findById(idCurso).orElse(null);
    }

    @Override
    public List<Curso> buscarTodos() {
        return cursoRepo.findAll();
    }

    @Override
    public Curso insertUno(Curso curso) {
        try {
            if (cursoRepo.existsById(curso.getIdCurso())) {
                return null;
            } else {
                return cursoRepo.save(curso);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateUno(Curso curso) {
        try {
            if (cursoRepo.existsById(curso.getIdCurso())) {
                cursoRepo.save(curso);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int deleteUno(Integer idCurso) {
        try {
            if (cursoRepo.existsById(idCurso)) {
                cursoRepo.deleteById(idCurso);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Curso> buscarPorEstado(Estado estado) {
        return cursoRepo.findByEstado(estado);
    }

    @Override
    public List<Curso> buscarPorActivos() {
        Date hoy = new Date(System.currentTimeMillis());
        return cursoRepo.findByEstadoAndFechaInicioAfter(Estado.ACEPTADO, hoy);
    }

	@Override
	public List<Curso> buscarPorMonitor(String emailMonitor) {
        return cursoRepo.findByMonitor_Email(emailMonitor);
	}

	@Override
	public boolean tieneConflictosDeHorario(String emailMonitor, int idCurso, List<PlanificacionCursoRequestDto> planificacionesNuevas) {
		    
		for (PlanificacionCursoRequestDto p : planificacionesNuevas) {
		        List<PlanificacionCurso> conflictos = planiRepo.findConflictosPlanificacion(
		            emailMonitor,
		            idCurso,
		            p.getDiaSemana(),
		            p.getHoraInicio(),
		            p.getHoraFin()
		        );
		        if (!conflictos.isEmpty()) {
		            return true;  // Hay conflicto
		        }
		    }
		    return false; // No hay conflictos
	}
}
