package web.modelo.services;

import web.dto.LoginRequestDto;
import web.dto.LoginResponseDto;
import web.dto.RegistroRequestDto;
import web.dto.RegistroResponseDto;
import web.modelo.entities.Usuario;

public interface AuthService extends CrudGenerico<Usuario, String>{
	
	LoginResponseDto login(LoginRequestDto loginDto);
	RegistroResponseDto altaUsuario (RegistroRequestDto registroDto);

}
