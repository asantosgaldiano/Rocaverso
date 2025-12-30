package web.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "DTO con la información del aforo actual del rocódromo")
public class AforoActualDto {

    @Schema(description = "Número máximo de personas permitidas en el rocódromo", example = "100")
    private int aforoMaximo;

    @Schema(description = "Número de usuarios actualmente dentro del rocódromo", example = "45")
    private int usuariosDentro;

    @Schema(description = "Porcentaje de ocupación actual respecto al aforo máximo", example = "45")
    private int porcentajeOcupado;
}
