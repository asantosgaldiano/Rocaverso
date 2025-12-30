package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import web.dto.AforoActualDto;
import web.modelo.services.AforoService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/aforo")
@Tag(name = "Aforo", description = "Operaciones relacionadas con el aforo del rocódromo")
public class AforoController {

    @Autowired
    private AforoService aforoService;

    @Operation(
            summary = "Obtener aforo actual",
            description = "Devuelve el aforo máximo, el número de usuarios dentro y el porcentaje de ocupación actual"
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aforo obtenido correctamente",
                content = @Content(schema = @Schema(implementation = AforoActualDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
    @GetMapping("/actual")
    public ResponseEntity<AforoActualDto> obtenerAforoActual() {
        AforoActualDto dto = aforoService.obtenerAforoActual();
        return ResponseEntity.ok(dto);
    }
    
}
