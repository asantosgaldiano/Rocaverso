package web.dto;

import java.sql.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.modelo.entities.Dificultad;
import web.modelo.entities.TipoVia;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información de una vía realizada por un usuario")
public class ViaRealizadaResponseDto {
    
    @Schema(description = "ID único de la vía realizada", example = "15")
    private int idViaRealizada;

    @Schema(description = "ID de la vía realizada", example = "12")
    private int idVia;

    @Schema(description = "Tipo de vía realizada", example = "DEPORTIVA")
    private TipoVia tipo;

    @Schema(description = "Dificultad de la vía realizada", example = "INTERMEDIO")
    private Dificultad dificultad;

    @Schema(description = "Nombre del usuario que realizó la vía", example = "Juan")
    private String nombreUsuario;

    @Schema(description = "Fecha en la que el usuario realizó la vía", example = "2025-08-20")
    private Date fechaRealizacion;
}
