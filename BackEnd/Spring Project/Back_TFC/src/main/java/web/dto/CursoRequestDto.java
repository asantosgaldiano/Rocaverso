package web.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

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
@Schema(description = "DTO de petición para crear o actualizar un curso")
public class CursoRequestDto {
	
    @Schema(description = "Nombre del curso", example = "Curso de Escalada Avanzado")
    private String nombre;

    @Schema(description = "Descripción del curso", example = "Curso para mejorar técnicas de escalada en rocódromo")
    private String descripcion;

    @Schema(description = "Fecha de inicio del curso", example = "2025-09-01")
    private Date fechaInicio;

    @Schema(description = "Fecha de fin del curso", example = "2025-12-15")
    private Date fechaFin;

    @Schema(description = "Número máximo de participantes", example = "20")
    private int aforoMaximo;

    @Schema(description = "Número mínimo de participantes necesarios para activar el curso", example = "5")
    private int minimoAsistencia;

    @Schema(description = "Precio del curso", example = "120.50")
    private BigDecimal precio;

    @Schema(description = "Lista de planificaciones del curso")
    private List<PlanificacionCursoRequestDto> planificaciones;

    @Schema(description = "Zona del rocódromo donde se realiza el curso", example = "ENTRENAMIENTO")
    private Zona zona;

    @Schema(description = "ID del rocódromo asociado al curso", example = "1")
    private int idRocodromo;

    @Schema(description = "Email del monitor encargado del curso", example = "monitor@rocodromo.com")
    private String emailMonitor;

}
