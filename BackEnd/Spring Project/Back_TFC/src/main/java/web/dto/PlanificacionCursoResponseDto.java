package web.dto;

import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import web.modelo.entities.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información de la planificación de un curso")
public class PlanificacionCursoResponseDto {
	
    @Schema(description = "ID único de la planificación", example = "5")
    private int idPlanificacion;

    @Schema(description = "Día de la semana en que se realiza la planificación", example = "LUNES")
    private DiaSemana diaSemana;

    @Schema(description = "Hora de inicio de la planificación", example = "10:00")
    private LocalTime horaInicio;

    @Schema(description = "Hora de finalización de la planificación", example = "12:00")
    private LocalTime horaFin;
}
