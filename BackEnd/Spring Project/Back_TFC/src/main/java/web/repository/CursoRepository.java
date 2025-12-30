package web.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import web.modelo.entities.Curso;
import web.modelo.entities.Estado;

public interface CursoRepository extends JpaRepository<Curso, Integer> {
    
    // Consultas personalizadas:
    List<Curso> findByEstado(Estado estado);
    
    List<Curso> findByEstadoAndFechaInicioAfter(Estado estado, Date fechaInicio);
    
    List<Curso> findByMonitor_Email(String emailMonitor);
}


