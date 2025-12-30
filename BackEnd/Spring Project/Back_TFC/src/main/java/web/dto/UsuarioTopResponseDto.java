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
@Schema(description = "DTO de respuesta con información de los usuarios top según vías realizadas")
public class UsuarioTopResponseDto {
	
    @Schema(description = "Correo electrónico del usuario", example = "usuario@correo.com")
    private String email;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellidos del usuario", example = "Pérez López")
    private String apellidos;

    @Schema(description = "Total de vías realizadas por el usuario", example = "45")
    private Long totalVias;
}
