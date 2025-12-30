package web.modelo.services;

import java.util.List;

import web.modelo.entities.Dificultad;
import web.modelo.entities.EstadoVia;
import web.modelo.entities.TipoVia;
import web.modelo.entities.Via;
import web.modelo.entities.Zona;

public interface ViaService extends CrudGenerico<Via, Integer> {
    
	
	// Aquí añadir métodos específicos
    List<Via> buscarPorTipo(TipoVia tipo);
    List<Via> buscarPorDificultad(Dificultad dificultad);
    List<Via> buscarPorEstado(EstadoVia estado);
    List<Via> buscarPorUbicacion(Zona ubicacion);
    List<Via> buscarFiltrado(TipoVia tipo, Dificultad dificultad, Zona zona);
    
}
