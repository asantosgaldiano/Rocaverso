package web.modelo.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.modelo.entities.Destacado;
import web.modelo.entities.Estado;
import web.modelo.entities.Evento;
import web.repository.EventoRepository;


@Service
public class EventoServiceImpl implements EventoService{

	@Autowired
	private EventoRepository eRepo;
	
	@Override
	public Evento buscarUno(Integer idEvento) {
		return eRepo.findById(idEvento).orElse(null);
	}

	@Override
	public List<Evento> buscarTodos() {
		return eRepo.findAll();
	}

	@Override
	public Evento insertUno(Evento evento) {
		try {
			if (eRepo.existsById(evento.getIdEvento())) {
				return null; // Me aseguro
				// Si lo encuentra devuelve nulo para no darlo de alta.
			}
			else 
				return eRepo.save(evento);
			
		} catch (Exception e) {
			e.printStackTrace(); 
			return null;
		}
	}

	@Override
	public int updateUno(Evento evento) {
	    try {
	        if (eRepo.existsById(evento.getIdEvento())) {
	            
	        	eRepo.save(evento);
	            return 1;
	        } else {
	            return 0; 
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1; 
	    }	
	}

	@Override
	public int deleteUno(Integer idEvento) {
	    try {
			if (eRepo.existsById(idEvento)) {
				eRepo.deleteById(idEvento); 
				return 1; // Si lo encuenta, y lo borra, devuelvo 1.
			}
			else 
				return 0; // Si no existe, devuelvo 0
				
		} catch (Exception e) {
			e.printStackTrace(); // para las pruebas hago un syso de todo lo ocurrido.
			return -1; // Si se casca, devuelvo -1
		}
	}

	@Override
	public List<Evento> buscarPorDestacados(Destacado destacado) {
		return eRepo.findByDestacado(destacado);
	}

	@Override
	public List<Evento> buscarPorEstado(Estado estado) {
		return eRepo.findByEstado(estado);
	}

	@Override
	public List<Evento> buscarPorActivos() {
		Date fechaActual = new Date();
		return eRepo.findByEstadoAndFechaInicioAfter(Estado.ACEPTADO, fechaActual);

	}

}
