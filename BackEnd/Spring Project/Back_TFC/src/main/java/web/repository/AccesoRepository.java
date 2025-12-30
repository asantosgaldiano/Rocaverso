package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import web.modelo.entities.Acceso;

public interface AccesoRepository extends JpaRepository<Acceso, Integer>{
    
	// Consulta JPQL usando entidades
	// Cuenta cuántos registros de la entidad Acceso no tienen hora de salida, es decir, cuántos usuarios siguen dentro.
	
	@Query("SELECT COUNT(a) FROM Acceso a WHERE a.fechaHoraSalida IS NULL")
    int contarUsuariosDentro();
}
