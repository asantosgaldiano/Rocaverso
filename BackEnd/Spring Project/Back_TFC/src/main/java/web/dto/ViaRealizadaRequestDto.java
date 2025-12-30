package web.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de solicitud para marcar una vía como realizada")
public class ViaRealizadaRequestDto {
	
    @Schema(description = "ID de la vía que el usuario ha realizado", example = "12")
    private int idVia;
}
