package web.modelo.services;

import java.util.List;

import web.dto.InscripcionResponseDto;
import web.dto.MisInscripcionesCalendarDto;
import web.dto.MisInscripcionesResponseDto;
import web.modelo.entities.Inscripcion;

public interface InscripcionService extends CrudGenerico<Inscripcion, Integer> {
    
	int inscribirseCurso(String emailUsuario, int idCurso);
    int cancelarInscripcion(String emailUsuario, int idCurso);
    int eliminarInscripcionesCurso(int idCurso);
    
    // Busquedas:
    List<MisInscripcionesResponseDto> misInscripciones(String emailUsuario);
    List<InscripcionResponseDto> findByCurso(int idCurso);
    List<MisInscripcionesCalendarDto> misInscripcionesParaCalendario(String emailUsuario);
}
