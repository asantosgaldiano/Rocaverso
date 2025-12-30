package web.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información de una reserva de evento")
public class ReservaResponseDto {
	
    @Schema(description = "ID único de la reserva", example = "15")
    private int idReserva;

    @Schema(description = "Nombre del usuario que realiza la reserva", example = "Juan")
    private String nombreUsuario;

    @Schema(description = "Apellidos del usuario que realiza la reserva", example = "Gómez Sánchez")
    private String apellidosUsuario;

    @Schema(description = "Fecha en la que se realizó la reserva", example = "2025-07-20")
    private Date fechaReserva;

    @Schema(description = "Nombre del evento reservado", example = "Competición de Escalada 2025")
    private String nombreEvento;

    @Schema(description = "Fecha de inicio del evento reservado", example = "2025-07-20")
    private Date fechaInicioEvento;
}
