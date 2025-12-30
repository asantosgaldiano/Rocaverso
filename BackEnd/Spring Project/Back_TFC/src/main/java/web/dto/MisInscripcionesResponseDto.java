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
@Schema(description = "DTO de respuesta con información de las inscripciones del usuario")
public class MisInscripcionesResponseDto {
	
    @Schema(description = "ID de la inscripción", example = "2")
    private int idInscripcion;

    @Schema(description = "Fecha en que se realizó la inscripción", example = "2025-06-10")
    private Date fechaInscripcion;

    @Schema(description = "Nombre del curso inscrito", example = "Curso de Escalada Avanzada")
    private String nombreCurso;

    @Schema(description = "Fecha de inicio del curso", example = "2025-07-01")
    private Date fechaInicioCurso;

    @Schema(description = "Fecha de finalización del curso", example = "2025-07-31")
    private Date fechaFinCurso;

    @Schema(description = "ID del curso asociado a la inscripción", example = "3")
    private int idCurso;
}
