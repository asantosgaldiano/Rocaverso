package web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de solicitud para registrar un nuevo usuario")
public class RegistroRequestDto {

    @Valid
    @Schema(description = "Datos del usuario a registrar")
    private UsuarioRequestDto usuario;
}
