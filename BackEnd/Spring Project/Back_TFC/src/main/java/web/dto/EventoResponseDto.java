package web.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.modelo.entities.Destacado;
import web.modelo.entities.Estado;
import web.modelo.entities.Zona;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información de un evento")
public class EventoResponseDto {
	
    @Schema(description = "ID único del evento", example = "12")
    private int idEvento;

    @Schema(description = "Nombre del evento", example = "Competición de Escalada 2025")
    private String nombre;

    @Schema(description = "Descripción detallada del evento", example = "Competición anual abierta a todos los niveles con premios y sorteos")
    private String descripcion;

    @Schema(description = "Fecha de inicio del evento", example = "2025-07-20")
    private Date fechaInicio;

    @Schema(description = "Hora de inicio del evento", example = "09:30")
    private LocalTime horaInicio;

    @Schema(description = "Hora de finalización del evento", example = "14:00")
    private LocalTime horaFin;

    @Schema(description = "Estado actual del evento", example = "ACEPTADO")
    private Estado estado;

    @Schema(description = "Indica si el evento está destacado", example = "S")
    private Destacado destacado;

    @Schema(description = "Número máximo de asistentes permitidos", example = "50")
    private int aforoMaximo;

    @Schema(description = "Número mínimo de participantes requeridos", example = "10")
    private int minimoAsistencia;

    @Schema(description = "Precio de inscripción al evento", example = "15.00")
    private BigDecimal precio;

    @Schema(description = "Nombre del archivo de imagen del evento", example = "competicion2025.jpg")
    private String imagen;

    @Schema(description = "Tipo de evento", example = "Charla")
    private String tipo;

    @Schema(description = "Número de plazas disponibles actualmente", example = "8")
    private int plazasLibres;

    @Schema(description = "Zona del rocódromo donde se celebra el evento", example = "ENTRENAMIENTO")
    private Zona zona;
}
