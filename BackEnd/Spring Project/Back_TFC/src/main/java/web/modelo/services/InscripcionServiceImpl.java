package web.modelo.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import web.dto.InscripcionResponseDto;
import web.dto.MisInscripcionesCalendarDto;
import web.dto.MisInscripcionesResponseDto;
import web.dto.PlanificacionCursoResponseDto;
import web.modelo.entities.Curso;
import web.modelo.entities.Inscripcion;
import web.modelo.entities.Usuario;
import web.repository.CursoRepository;
import web.repository.InscripcionRepository;
import web.repository.UsuarioRepository;

@Service
public class InscripcionServiceImpl implements InscripcionService {

    @Autowired
    private InscripcionRepository iRepo;

    @Autowired
    private UsuarioRepository uRepo;

    @Autowired
    private CursoRepository cRepo;

    @Override
    public int inscribirseCurso(String emailUsuario, int idCurso) {
        try {
            Usuario usuario = uRepo.findById(emailUsuario).orElse(null);
            Curso curso = cRepo.findById(idCurso).orElse(null);

            if (usuario == null || curso == null) return 4; // Si no existe usuario o curso

            if (iRepo.existsByUsuarioAndCurso(usuario, curso)) return 2; // Compruebo si el usuario ya está inscrito

            long inscritos = iRepo.countByCurso(curso);
            if (inscritos >= curso.getAforoMaximo()) return 3; // Si se supera aforo máximo

            Inscripcion inscripcion = Inscripcion.builder()
                .usuario(usuario)
                .curso(curso)
                .fechaInscripcion(new Date(System.currentTimeMillis()))
                .build();

            iRepo.save(inscripcion);
            return 1; // Si inscripción correcta
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    @Override
    public int cancelarInscripcion(String emailUsuario, int idCurso) {
        try {
            Usuario usuario = uRepo.findById(emailUsuario).orElse(null);
            Curso curso = cRepo.findById(idCurso).orElse(null);

            if (usuario == null || curso == null) return 4; // Si no existe usuario o curso

            if (!iRepo.existsByUsuarioAndCurso(usuario, curso)) return 5; // Compruebo si el usuario ya está inscrito

            List<Inscripcion> inscripciones = iRepo.findByUsuario(usuario);
            Inscripcion inscripcion = inscripciones.stream()
                .filter(i -> i.getCurso().getIdCurso() == idCurso)
                .findFirst().orElse(null);

            if (inscripcion != null) {
                iRepo.delete(inscripcion);
                return 1; // Si encuentro la inscripción, eliminarla y devuelo 1.
            }

            return 4; // Si no se encuentra la inscripción
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    @Override
    public List<MisInscripcionesResponseDto> misInscripciones(String emailUsuario) {
        Usuario usuario = uRepo.findById(emailUsuario).orElse(null);
        if (usuario == null) return List.of(); // Si no existe, devuelvo lista vacía

        return iRepo.findByUsuario(usuario).stream()
            .map(i -> MisInscripcionesResponseDto.builder()
                .idInscripcion(i.getIdInscripcion())
                .fechaInscripcion(i.getFechaInscripcion())
                .nombreCurso(i.getCurso().getNombre())
                .fechaInicioCurso(i.getCurso().getFechaInicio())
                .fechaFinCurso(i.getCurso().getFechaFin())
                .idCurso(i.getCurso().getIdCurso())
                .build()
            ).toList();
    }
    
	@Override
	public List<MisInscripcionesCalendarDto> misInscripcionesParaCalendario(String emailUsuario) {
        Usuario usuario = uRepo.findById(emailUsuario).orElse(null);
        if (usuario == null) return List.of(); // Si no existe, devuelvo lista vacía
        
        List<Inscripcion> inscripciones = iRepo.findByUsuario(usuario);

        // Transformo cada inscripción en DTO para calendario
        return inscripciones.stream().map(inscripcion -> {
            Curso curso = inscripcion.getCurso();
            
            // Transformo las planificaciones del curso en DTOs
            List<PlanificacionCursoResponseDto> planificaciones = curso.getPlanificaciones().stream()
                .map(planificacion -> PlanificacionCursoResponseDto.builder()
                    .idPlanificacion(planificacion.getIdPlanificacion())
                    .diaSemana(planificacion.getDiaSemana())
                    .horaInicio(planificacion.getHoraInicio())
                    .horaFin(planificacion.getHoraFin())
                    .build()
                ).collect(Collectors.toList());

            // Creo DTO de inscripción para calendario incluyendo las planificaciones
            return MisInscripcionesCalendarDto.builder()
                .nombreCurso(curso.getNombre())
                .fechaInicioCurso(curso.getFechaInicio())
                .fechaFinCurso(curso.getFechaFin())
                .planificaciones(planificaciones)
                .build();
        }).collect(Collectors.toList());
	}
	
	
    @Override
    public int eliminarInscripcionesCurso(int idCurso) {
        try {
            Curso curso = cRepo.findById(idCurso).orElse(null);
            if (curso == null) return 0; // Si el curso no existe

            List<Inscripcion> inscripciones = iRepo.findByCurso(curso);
            if (inscripciones.isEmpty()) return 2; // Si no hay inscripciones

            iRepo.deleteAll(inscripciones);
            return 1; // Si la eliminación fue correcta
            
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

	@Override
	public List<InscripcionResponseDto> findByCurso(int idCurso) {
        Curso curso = cRepo.findById(idCurso)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

            List<Inscripcion> inscripciones = iRepo.findByCurso(curso);

            return inscripciones.stream()
                .map(inscripcion -> InscripcionResponseDto.builder()
                    .idInscripcion(inscripcion.getIdInscripcion())
                    .fechaInscripcion(inscripcion.getFechaInscripcion())
                    .nombreCurso(curso.getNombre())
                    .fechaInicioCurso(curso.getFechaInicio())
                    .nombreUsuario(inscripcion.getUsuario().getNombre())
                    .apellidosUsuario(inscripcion.getUsuario().getApellidos())
                    .build()
                ).toList();
    }
    
	@Override
	public Inscripcion buscarUno(Integer clavePk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Inscripcion> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inscripcion insertUno(Inscripcion entidad) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateUno(Inscripcion entidad) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteUno(Integer clavePk) {
		// TODO Auto-generated method stub
		return 0;
	}




}


