package web.modelo.services;

import web.dto.EstadisticasUsuarioDto;

public interface EstadisticasService {
	
    EstadisticasUsuarioDto obtenerEstadisticasUsuario(String email);
}
