package web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.modelo.entities.Tipo;
import web.modelo.services.TipoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/tipo")
@Tag(name = "Tipos", description = "Operaciones relacionadas con los tipos de eventos")
public class TipoController {

	@Autowired
	TipoService tipoService;

    @Operation(summary = "Listar todos los tipos", description = "Devuelve la lista completa de tipos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipos obtenidos correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tipo.class),
                examples = @ExampleObject(value = "[{\"idTipo\":1,\"nombre\":\"Bloque\"},{\"idTipo\":2,\"nombre\":\"Top-Rope\"}]"))
        ),
        @ApiResponse(responseCode = "404", description = "No se encontraron tipos")
    })
    @GetMapping("/all")
    public ResponseEntity<?> listarTipos() {
    	List<Tipo> response = tipoService.buscarTodos();
		 return ResponseEntity.ok(response);
		 
    }
	
}
