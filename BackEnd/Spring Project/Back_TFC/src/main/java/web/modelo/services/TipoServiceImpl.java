package web.modelo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.modelo.entities.Tipo;
import web.repository.TipoRepository;

@Service
public class TipoServiceImpl implements TipoService{

	@Autowired
	private TipoRepository tRepo;
	
	@Override
	public Tipo buscarUno(Integer idTipo) {
		return tRepo.findById(idTipo).orElse(null);
	}

	@Override
	public List<Tipo> buscarTodos() {
		return tRepo.findAll();
	}

	@Override
	public Tipo insertUno(Tipo tipo) {
		try {
			if (tRepo.existsById(tipo.getIdTipo())) {
				return null; // Me aseguro
				// Si lo encuentra devuelve nulo para no darlo de alta.
			}
			else 
				return tRepo.save(tipo);
			
		} catch (Exception e) {
			e.printStackTrace(); 
			return null;
		}
	}

	@Override
	public int updateUno(Tipo tipo) {
	    try {
	        if (tRepo.existsById(tipo.getIdTipo())) {
	            
	        	tRepo.save(tipo);
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
	public int deleteUno(Integer idTipo) {
	    try {
			if (tRepo.existsById(idTipo)) {
				tRepo.deleteById(idTipo); 
				return 1; // Si lo encuenta, y lo borra, devuelvo 1.
			}
			else 
				return 0; // Si no existe, devuelvo 0
				
		} catch (Exception e) {
			e.printStackTrace(); // para las pruebas hago un syso de todo lo ocurrido.
			return -1; // Si se casca, devuelvo -1
		}
	}

}
