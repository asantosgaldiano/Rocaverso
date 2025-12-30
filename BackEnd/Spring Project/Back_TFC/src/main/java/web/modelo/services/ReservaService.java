package web.modelo.services;

import java.util.List;

import web.dto.MisReservasResponseDto;
import web.dto.ReservaResponseDto;
import web.modelo.entities.Reserva;

public interface ReservaService extends CrudGenerico<Reserva, Integer> {

    int reservarEvento(String emailUsuario, int idEvento);
    int cancelarReservaEvento(String emailUsuario, int idEvento); 
    int eliminarReservasEvento(int idEvento);
    
    // Busquedas:
    List<MisReservasResponseDto> misReservas(String emailUsuario);
    List<ReservaResponseDto> findByEvento(int idEvento);
}
