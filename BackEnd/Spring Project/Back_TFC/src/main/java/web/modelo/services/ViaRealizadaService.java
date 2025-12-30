package web.modelo.services;

import java.util.List;

import web.modelo.entities.ViaRealizada;

public interface ViaRealizadaService extends CrudGenerico<ViaRealizada, Integer>{

	// Busquedas:
    List<ViaRealizada> buscarPorEmailUsuario(String email);
    List<Object[]> topViasRealizadas();   
    List<Object[]> topUsuarios();
    
    
}
