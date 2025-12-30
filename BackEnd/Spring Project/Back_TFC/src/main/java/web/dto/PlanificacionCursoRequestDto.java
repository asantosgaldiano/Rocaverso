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
@Schema(description = "DTO de petición para una planificación de curso")
public class PlanificacionCursoRequestDto {
	
    @Schema(description = "Día de la semana en que se realiza la planificación", example = "LUNES")
    private DiaSemana diaSemana;

    @Schema(description = "Hora de inicio de la planificación", example = "17:30")
    private LocalTime horaInicio;

    @Schema(description = "Hora de fin de la planificación", example = "19:00")
    private LocalTime horaFin;

    @Schema(description = "ID del curso al que pertenece esta planificación", example = "3")
    private int idCurso;
}
