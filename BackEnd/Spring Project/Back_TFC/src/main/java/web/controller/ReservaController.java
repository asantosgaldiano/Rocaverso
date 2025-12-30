package web.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import web.dto.ReservaResponseDto;
import web.modelo.services.ReservaService;
import web.modelo.services.UsuarioService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/reserva")
@Tag(name = "Reserva de Eventos", description = "Operaciones relacionadas con reservas de eventos")
public class ReservaController {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	ReservaService reservaService;

	@Autowired
	private ModelMapper modelMapper;

    @Operation(summary = "Cancelar reserva de un evento", description = "Permite al usuario cancelar su reserva para un evento específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva cancelada correctamente"),
        @ApiResponse(responseCode = "404", description = "Reserva o evento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno al cancelar la reserva")
    })
	@DeleteMapping("/cancelar/{idEvento}")
	public ResponseEntity<?> cancelarReserva(@PathVariable int idEvento) {
		
		// Obtener usuario autenticado
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String emailToken = auth.getName();

	    // Cancelar reserva evento
	    int resultado = reservaService.cancelarReservaEvento(emailToken, idEvento);

	    // Respuestas
	    switch (resultado) {
	        case 1: 
	            return ResponseEntity.ok().build();
	        case 4:
	            return ResponseEntity.notFound().build();
	        case 5:
	            return ResponseEntity.notFound().build();
	        default:
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

    @Operation(summary = "Obtener reservas de un evento", description = "Devuelve todas las reservas asociadas a un evento específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservas obtenidas correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservaResponseDto.class),
                examples = @ExampleObject(value = "[{\"idReserva\":1,\"emailUsuario\":\"usuario@example.com\",\"idEvento\":2}]"))
        ),
        @ApiResponse(responseCode = "404", description = "No se encontraron reservas para este evento")
    })
    @GetMapping("/evento/{idEvento}")
    public ResponseEntity<?> getReservasByEvento(@PathVariable int idEvento) {
    	
    	// Obtener reservas de evento
        List<ReservaResponseDto> reservas = reservaService.findByEvento(idEvento);
        return ResponseEntity.ok(reservas);
    }
}
