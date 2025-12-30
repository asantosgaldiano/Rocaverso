package web.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.modelo.entities.Dificultad;
import web.modelo.entities.EstadoVia;
import web.modelo.entities.TipoVia;
import web.modelo.entities.Zona;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información de una vía")
public class ViaResponseDto {

    @Schema(description = "ID único de la vía", example = "10")
    private int idVia;

    @Schema(description = "Dificultad de la vía", example = "INTERMEDIO")
    private Dificultad dificultad;

    @Schema(description = "Tipo de vía", example = "DEPORTIVA")
    private TipoVia tipo;

    @Schema(description = "Estado de la vía", example = "ACTIVA")
    private EstadoVia estado;

    @Schema(description = "Ubicación o zona de la vía dentro del rocódromo", example = "ENTRENAMIENTO")
    private Zona ubicacion;
    
}
