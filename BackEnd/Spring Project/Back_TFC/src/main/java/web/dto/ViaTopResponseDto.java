package web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.modelo.entities.Dificultad;
import web.modelo.entities.TipoVia;
import web.modelo.entities.Zona;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con información de las vías más realizadas")
public class ViaTopResponseDto {

    @Schema(description = "ID único de la vía", example = "15")
    private int idVia;

    @Schema(description = "Tipo de vía", example = "DEPORTIVA")
    private TipoVia tipo;

    @Schema(description = "Dificultad de la vía", example = "AVANZADO")
    private Dificultad dificultad;

    @Schema(description = "Ubicación o zona de la vía dentro del rocódromo", example = "ENTRENAMIENTO")
    private Zona ubicacion;

    @Schema(description = "Número total de veces que la vía ha sido realizada", example = "120")
    private long totalRealizaciones;
}
