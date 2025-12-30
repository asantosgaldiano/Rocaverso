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
@Schema(description = "DTO de respuesta al iniciar sesión, incluyendo token y datos del usuario")
public class LoginResponseDto {

    @Schema(description = "Correo electrónico del usuario", example = "usuario@correo.com")
    private String email;

    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    private String nombre;

    @Schema(description = "Tipo de rol del usuario", example = "USUARIO")
    private String tipoRol;

    @Schema(description = "Token JWT generado tras el inicio de sesión", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Estado del usuario: 1 habilitado, 0 deshabilitado", example = "1")
    private int enabled;
    
}
