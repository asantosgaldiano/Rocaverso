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
@Schema(description = "DTO de solicitud para la creación o actualización de un usuario")
public class UsuarioRequestDto {

    @Schema(description = "Correo electrónico del usuario", example = "usuario@correo.com")
    private String email;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellidos del usuario", example = "Gómez Sánchez")
    private String apellidos;

    @Schema(description = "Contraseña del usuario", example = "Password123!")
    private String password;
}