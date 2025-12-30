package web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import web.modelo.entities.Curso;
import web.modelo.entities.Inscripcion;
import web.modelo.entities.Usuario;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    
	// Consultas personalizadas:
	boolean existsByUsuarioAndCurso(Usuario usuario, Curso curso);
    int countByCurso(Curso curso);
    List<Inscripcion> findByUsuario(Usuario usuario);
    List<Inscripcion> findByCurso(Curso curso);

}
