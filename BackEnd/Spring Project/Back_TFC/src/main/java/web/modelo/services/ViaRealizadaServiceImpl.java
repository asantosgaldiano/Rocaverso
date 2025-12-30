package web.modelo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.modelo.entities.ViaRealizada;
import web.repository.ViaRealizadaRepository;

@Service
public class ViaRealizadaServiceImpl implements ViaRealizadaService{

    @Autowired
    private ViaRealizadaRepository viaRealizadaRepo;
	
    @Override
    public ViaRealizada buscarUno(Integer id) {
        return viaRealizadaRepo.findById(id).orElse(null);
    }

    @Override
    public List<ViaRealizada> buscarTodos() {
        return viaRealizadaRepo.findAll();
    }

    @Override
    public ViaRealizada insertUno(ViaRealizada entity) {
        try {
            if (viaRealizadaRepo.existsById(entity.getIdViaRealizada())) {
                return null; // No insertar si ya existe el id
            } else {
                return viaRealizadaRepo.save(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateUno(ViaRealizada entity) {
        try {
            if (viaRealizadaRepo.existsById(entity.getIdViaRealizada())) {
                viaRealizadaRepo.save(entity);
                return 1; // Éxito
            } else {
                return 0; // No existe para actualizar
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    @Override
    public int deleteUno(Integer id) {
        try {
            if (viaRealizadaRepo.existsById(id)) {
                viaRealizadaRepo.deleteById(id);
                return 1; // Éxito
            } else {
                return 0; // No existe para borrar
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

	@Override
	public List<ViaRealizada> buscarPorEmailUsuario(String email) {
        return viaRealizadaRepo.findByUsuarioEmail(email);
    }

	@Override
	public List<Object[]> topViasRealizadas() {
	    return viaRealizadaRepo.findTopViasRealizadas();
	}

	@Override
	public List<Object[]> topUsuarios() {
	    return viaRealizadaRepo.findTopUsuarios();
	}

}
