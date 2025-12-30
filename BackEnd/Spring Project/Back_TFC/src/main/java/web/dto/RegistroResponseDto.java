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
@Schema(description = "DTO de respuesta al registrar un nuevo usuario")
public class RegistroResponseDto {

    @Schema(description = "Información del usuario registrado")
    private UsuarioResponseDto usuario;

    @Schema(description = "Token JWT generado tras el registro", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}