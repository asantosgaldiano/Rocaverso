package web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import web.modelo.entities.Evento;
import web.modelo.entities.Reserva;
import web.modelo.entities.Usuario;

public interface ReservaRepository extends JpaRepository<Reserva, Integer>{
	
	// Consultas personalizadas:
    int countByEvento(Evento evento);
    boolean existsByUsuarioAndEvento(Usuario usuario, Evento evento);
    List<Reserva> findByUsuario(Usuario usuario);
    List<Reserva> findByEvento(Evento evento);
}
