package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.modelo.entities.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, String> {

	// Consultas personalizadas:
	Usuario findByEmail(String email);
}
