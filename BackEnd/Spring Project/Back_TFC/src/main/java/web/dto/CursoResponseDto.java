package web.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.modelo.entities.Estado;
import web.modelo.entities.Zona;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información de un curso")
public class CursoResponseDto {
	
    @Schema(description = "ID único del curso", example = "5")
    private int idCurso;

    @Schema(description = "Nombre del curso", example = "Curso de Escalada Intermedio")
    private String nombre;

    @Schema(description = "Descripción del curso", example = "Curso de escalada en rocódromo para nivel intermedio")
    private String descripcion;

    @Schema(description = "Fecha de inicio del curso", example = "2025-09-01")
    private Date fechaInicio;

    @Schema(description = "Fecha de fin del curso", example = "2025-12-15")
    private Date fechaFin;

    @Schema(description = "Estado actual del curso", example = "ACEPTADO")
    private Estado estado;

    @Schema(description = "Número máximo de participantes permitidos", example = "20")
    private int aforoMaximo;

    @Schema(description = "Número mínimo de participantes necesarios", example = "5")
    private int minimoAsistencia;

    @Schema(description = "Precio del curso", example = "120.50")
    private BigDecimal precio;

    @Schema(description = "Lista de planificaciones asociadas al curso")
    private List<PlanificacionCursoResponseDto> planificaciones;

    @Schema(description = "Número de plazas disponibles actualmente", example = "8")
    private int plazasLibres;

    @Schema(description = "Zona del rocódromo donde se realiza el curso", example = "ENTRENAMIENTO")
    private Zona zona;

    @Schema(description = "Nombre del monitor encargado del curso", example = "Juan Pérez")
    private String nombreMonitor;
}
