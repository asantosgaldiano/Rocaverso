package web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import web.dto.InscripcionResponseDto;
import web.modelo.services.InscripcionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/inscripcion")
@Tag(name = "Inscripción", description = "Operaciones relacionadas con inscripciones a cursos")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @Operation(summary = "Cancelar inscripción", description = "Permite al usuario cancelar su inscripción a un curso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inscripción cancelada correctamente"),
        @ApiResponse(responseCode = "404", description = "Inscripción no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/cancelar/{idCurso}")
    public ResponseEntity<?> cancelar(@PathVariable int idCurso) {
    	
    	// Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Cancelar Inscripcion
        int resultado = inscripcionService.cancelarInscripcion(email, idCurso);
        switch (resultado) {
	        case 1: 
	            return ResponseEntity.ok().build();
	        case 4:
	            return ResponseEntity.notFound().build();
	        case 5:
	            return ResponseEntity.notFound().build();
	        default:
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  
        }
    }

    @Operation(summary = "Obtener inscripciones por curso", description = "Devuelve todas las inscripciones de un curso por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de inscripciones obtenida correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InscripcionResponseDto.class)))
    })
    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<?> getInscripcionesByCurso(@PathVariable int idCurso) {
    	
        List<InscripcionResponseDto> lista = inscripcionService.findByCurso(idCurso);
        
        return ResponseEntity.ok(lista);
    }
}
