package web.modelo.services;

import java.util.List;
import web.modelo.entities.Destacado;
import web.modelo.entities.Estado;
import web.modelo.entities.Evento;

public interface EventoService extends CrudGenerico<Evento, Integer> {

	// Busquedas:
	List<Evento> buscarPorDestacados(Destacado destacado);
	
	List<Evento> buscarPorEstado(Estado estado);
	
	List<Evento> buscarPorActivos();
}
