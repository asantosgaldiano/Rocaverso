package web.dto;

import java.sql.Date;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con información de las reservas de eventos del usuario")
public class MisReservasResponseDto {
	
    @Schema(description = "ID de la reserva", example = "1")
    private int idReserva;

    @Schema(description = "Fecha en que se realizó la reserva", example = "2025-06-15")
    private Date fechaReserva;

    @Schema(description = "Nombre del evento reservado", example = "Competición de Escalada 2025")
    private String nombreEvento;

    @Schema(description = "Tipo de evento", example = "Charla")
    private String tipoEvento;

    @Schema(description = "Fecha de inicio del evento", example = "2025-07-20")
    private Date fechaInicioEvento;

    @Schema(description = "Hora de inicio del evento", example = "09:30")
    private LocalTime horaInicio;

    @Schema(description = "Hora de finalización del evento", example = "14:00")
    private LocalTime horaFin;

    @Schema(description = "ID del evento asociado a la reserva", example = "2")
    private int idEvento;
    
}
