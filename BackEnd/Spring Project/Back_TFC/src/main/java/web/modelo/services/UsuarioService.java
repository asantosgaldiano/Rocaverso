package web.modelo.services;

import web.modelo.entities.Usuario;

public interface UsuarioService extends CrudGenerico<Usuario, String> {

	// Busquedas:
	Usuario buscarPorEmail(String email);

}
