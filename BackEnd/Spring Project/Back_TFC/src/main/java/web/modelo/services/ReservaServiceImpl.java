package web.modelo.services;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import web.dto.MisReservasResponseDto;
import web.dto.ReservaResponseDto;
import web.modelo.entities.Evento;
import web.modelo.entities.Reserva;
import web.modelo.entities.Usuario;
import web.repository.EventoRepository;
import web.repository.ReservaRepository;
import web.repository.UsuarioRepository;

@Service
public class ReservaServiceImpl implements ReservaService{

	@Autowired
	private ReservaRepository rRepo;
	
	@Autowired
	private EventoRepository eRepo;
	
	@Autowired
	private UsuarioRepository uRepo;

	@Override
	public int reservarEvento(String emailUsuario, int idEvento) {
	    try {
	        Usuario usuario = uRepo.findById(emailUsuario).orElse(null);
	        Evento evento = eRepo.findById(idEvento).orElse(null);

	        if (usuario == null || evento == null) {
	            return 4; // No encontrado
	        }

	        if (rRepo.existsByUsuarioAndEvento(usuario, evento)) {
	            return 2; // Ya inscrito
	        }

	        long reservasActuales = rRepo.countByEvento(evento);
	        if (reservasActuales >= evento.getAforoMaximo()) {
	            return 3; // Aforo Completo
	        }

	        Reserva reserva = Reserva.builder()
	                .usuario(usuario)
	                .evento(evento)
	                .fechaReserva(new Date(System.currentTimeMillis()))
	                .build();

	        rRepo.save(reserva);
	        return 1; // OK
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1; // ERROR
	    }
	}
	
	@Override
	public int cancelarReservaEvento(String emailUsuario, int idEvento) {
	    try {
	        Usuario usuario = uRepo.findById(emailUsuario).orElse(null);
	        Evento evento = eRepo.findById(idEvento).orElse(null);

	        if (usuario == null || evento == null) {
	            return 4; // No encontrado
	        }

	        if (!rRepo.existsByUsuarioAndEvento(usuario, evento)) {
	            return 5; // No reservado
	        }

	        List<Reserva> reservasUsuario = rRepo.findByUsuario(usuario);
	        Reserva reservaAEliminar = reservasUsuario.stream()
	                .filter(r -> r.getEvento().getIdEvento() == idEvento)
	                .findFirst()
	                .orElse(null);

	        if (reservaAEliminar == null) {
	            return 4; // No Encontrado
	        }

	        rRepo.delete(reservaAEliminar);
	        return 1; // OK
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1; // ERROR
	    }
	}
	
	@Override
	public Reserva buscarUno(Integer idReserva) {
		return rRepo.findById(idReserva).orElse(null);
	}

	@Override
	public List<Reserva> buscarTodos() {
		return rRepo.findAll();
	}

	@Override
	public Reserva insertUno(Reserva reserva) {
		try {
			if (rRepo.existsById(reserva.getIdReserva())) {
				return null; // Me aseguro
				// Si lo encuentra devuelve nulo para no darlo de alta.
			}
			else 
				return rRepo.save(reserva);
			
		} catch (Exception e) {
			e.printStackTrace(); 
			return null;
		}
	}

	@Override
	public int updateUno(Reserva reserva) {
	    try {
	        if (rRepo.existsById(reserva.getIdReserva())) {
	            
	        	rRepo.save(reserva);
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
	public int deleteUno(Integer idReserva) {
	    try {
			if (rRepo.existsById(idReserva)) {
				rRepo.deleteById(idReserva); 
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
	public int eliminarReservasEvento(int idEvento) {
	    try {
	        Evento evento = eRepo.findById(idEvento).orElse(null);

	        if (evento == null) {
	            return 0; // Evento no encontrado
	        }

	        List<Reserva> reservas = rRepo.findByEvento(evento);

	        if (reservas.isEmpty()) {
	            return 2; // No había reservas para este evento
	        }

	        rRepo.deleteAll(reservas);
	        return 1; // Reservas eliminadas correctamente
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1; // Error
	    }
	}

	@Override
	public List<MisReservasResponseDto> misReservas(String emailUsuario) {
	    Usuario usuario = uRepo.findById(emailUsuario).orElse(null);
	    
	    if (usuario == null) return List.of();

	    List<Reserva> reservas = rRepo.findByUsuario(usuario);

	    return reservas.stream()
	            .map(r -> MisReservasResponseDto.builder()
	                    .idReserva(r.getIdReserva())
	                    .fechaReserva(r.getFechaReserva())
	                    .nombreEvento(r.getEvento().getNombre())
	                    .horaInicio(r.getEvento().getHoraInicio())
	                    .horaFin(r.getEvento().getHoraFin())
	                    .tipoEvento(r.getEvento().getTipo().getNombre())
	                    .fechaInicioEvento(r.getEvento().getFechaInicio())
	                    .idEvento(r.getEvento().getIdEvento())
	                    .build()
	            )
	            .toList();
	}

	@Override
	public List<ReservaResponseDto> findByEvento(int idEvento) {
        Evento evento = eRepo.findById(idEvento)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

            List<Reserva> reservas = rRepo.findByEvento(evento);

            return reservas.stream()
                .map(reserva -> ReservaResponseDto.builder()
                    .idReserva(reserva.getIdReserva())
                    .nombreUsuario(reserva.getUsuario().getNombre())
                    .apellidosUsuario(reserva.getUsuario().getApellidos())
                    .fechaReserva(reserva.getFechaReserva())
                    .nombreEvento(evento.getNombre())
                    .fechaInicioEvento(evento.getFechaInicio())
                    .build()
                ).toList();
        
	}




}


