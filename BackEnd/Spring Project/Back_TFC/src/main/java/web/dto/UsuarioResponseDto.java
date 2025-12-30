package web.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información de un usuario")
public class UsuarioResponseDto {

    @Schema(description = "Correo electrónico del usuario", example = "usuario@correo.com")
    private String email;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellidos del usuario", example = "Pérez López")
    private String apellidos;

    @Schema(description = "Fecha en la que se registró el usuario", example = "2025-05-12")
    private Date fechaRegistro;

    @Schema(description = "Rol asignado al usuario", example = "USUARIO")
    private String rol;

    @Schema(description = "Indica si el usuario está habilitado (1 = habilitado, 0 = deshabilitado)", example = "1")
    private Integer enabled;
}
