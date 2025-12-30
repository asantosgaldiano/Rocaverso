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
@Schema(description = "DTO de respuesta con la información de una inscripción a un curso")
public class InscripcionResponseDto {
	
    @Schema(description = "ID único de la inscripción", example = "2")
    private int idInscripcion;

    @Schema(description = "Fecha en la que se realizó la inscripción", example = "2025-05-12")
    private Date fechaInscripcion;

    @Schema(description = "Nombre del curso inscrito", example = "Curso de Escalada Avanzada")
    private String nombreCurso;

    @Schema(description = "Fecha de inicio del curso", example = "2025-07-01")
    private Date fechaInicioCurso;

    @Schema(description = "Nombre del usuario inscrito", example = "Juan")
    private String nombreUsuario;

    @Schema(description = "Apellidos del usuario inscrito", example = "Pérez Gómez")
    private String apellidosUsuario;
}
