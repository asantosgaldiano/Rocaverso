package web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import web.modelo.entities.ViaRealizada;

public interface ViaRealizadaRepository extends JpaRepository<ViaRealizada, Integer> {
    
	// Consultas personalizadas:
	List<ViaRealizada> findByUsuarioEmail(String email);
	
	// Agrupa las vías realizadas (v.via.idVia) y cuenta cuántas veces se ha realizado cada una.
	// Luego las ordena de mayor a menor, mostrando las vías más populares.
	@Query("SELECT v.via.idVia AS idVia, COUNT(v) AS totalRealizaciones " +
		       "FROM ViaRealizada v " +
		       "GROUP BY v.via.idVia " +
		       "ORDER BY totalRealizaciones DESC")
	List<Object[]> findTopViasRealizadas();
	
	// Agrupa por usuario (v.usuario.email) y cuenta cuántas vías ha realizado cada uno.
	// También ordena de mayor a menor, mostrando los usuarios más activos.
	@Query("SELECT v.usuario.email AS email, COUNT(v) AS total " +
		       "FROM ViaRealizada v " +
		       "GROUP BY v.usuario.email " +
		       "ORDER BY total DESC")
	List<Object[]> findTopUsuarios();

}
