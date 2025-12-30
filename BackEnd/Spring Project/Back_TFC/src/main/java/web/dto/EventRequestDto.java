package web.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.modelo.entities.Zona;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de petición para crear o actualizar un evento")
public class EventRequestDto {
	
    @Schema(description = "Nombre del evento", example = "Competición de Escalada 2025")
    private String nombre;

    @Schema(description = "Descripción del evento", example = "Evento anual abierto a todos los niveles")
    private String descripcion;

    @Schema(description = "Fecha de inicio del evento", example = "2025-07-20")
    private Date fechaInicio;

    @Schema(description = "Hora de inicio del evento", example = "09:30")
    private LocalTime horaInicio;

    @Schema(description = "Hora de finalización del evento", example = "14:00")
    private LocalTime horaFin;

    @Schema(description = "Número máximo de asistentes permitidos", example = "50")
    private int aforoMaximo;

    @Schema(description = "Número mínimo de participantes requeridos", example = "10")
    private int minimoAsistencia;

    @Schema(description = "Precio de inscripción al evento", example = "15.00")
    private BigDecimal precio;

    @Schema(description = "Nombre del archivo de imagen asociado al evento", example = "competicion2025.jpg")
    private String imagen;

    @Schema(description = "ID del tipo de evento asociado", example = "3")
    private int idTipo;

    @Schema(description = "Zona del rocódromo donde se celebrará el evento", example = "ENTRENAMIENTO")
    private Zona zona;

    @Schema(description = "ID del rocódromo donde se celebra el evento", example = "1")
    private int idRocodromo;
}
