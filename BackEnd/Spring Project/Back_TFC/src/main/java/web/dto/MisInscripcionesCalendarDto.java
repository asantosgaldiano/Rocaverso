package web.dto;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO con la información de las inscripciones del usuario en formato calendario")
public class MisInscripcionesCalendarDto {
	
    @Schema(description = "Nombre del curso inscrito", example = "Curso de Escalada Avanzada")
    private String nombreCurso;

    @Schema(description = "Fecha de inicio del curso", example = "2025-07-01")
    private Date fechaInicioCurso;

    @Schema(description = "Fecha de finalización del curso", example = "2025-07-31")
    private Date fechaFinCurso;

    @Schema(description = "Planificaciones asociadas al curso")
    private List<PlanificacionCursoResponseDto> planificaciones;
}
