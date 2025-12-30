package web.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.dto.PlanificacionCursoRequestDto;
import web.dto.PlanificacionCursoResponseDto;
import web.modelo.entities.Curso;
import web.modelo.entities.PlanificacionCurso;
import web.modelo.services.CursoService;
import web.modelo.services.PlanificacionCursoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/planificacion")
@Tag(name = "Planificación de Cursos", description = "Operaciones relacionadas con la planificación de cursos")
public class PlanificacionCursoController {

    @Autowired
    private PlanificacionCursoService planificacionService;
    
    @Autowired
    private CursoService cursoService;
    
    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Obtener todas las planificaciones", description = "Devuelve todas las planificaciones de cursos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de planificaciones obtenida correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanificacionCursoResponseDto.class))
        )
    })    
    @GetMapping("/all")
    public ResponseEntity<List<PlanificacionCursoResponseDto>> getAll() {
    	
        List<PlanificacionCurso> lista = planificacionService.buscarTodos();
        
        List<PlanificacionCursoResponseDto> response = lista.stream()
            .map(p -> modelMapper.map(p, PlanificacionCursoResponseDto.class))
            .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener planificación por ID", description = "Devuelve la planificación de un curso según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Planificación encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanificacionCursoResponseDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "Planificación no encontrada")
    })
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getOne(@PathVariable int id) {
    	
    	// Buscar la planificación por ID
        PlanificacionCurso p = planificacionService.buscarUno(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Convertir a Dto
        PlanificacionCursoResponseDto dto = modelMapper.map(p, PlanificacionCursoResponseDto.class);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Crear planificación", description = "Crea una nueva planificación para un curso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Planificación creada correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanificacionCursoResponseDto.class),
                examples = @ExampleObject(value = "{ \"idPlanificacion\": 1, \"diaSemana\": \"Lunes\", \"horaInicio\": \"10:00\", \"horaFin\": \"12:00\", \"idCurso\": 1 }"))
        ),
        @ApiResponse(responseCode = "400", description = "Curso no válido o datos incorrectos")
    })
    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody PlanificacionCursoRequestDto requestDto) {
    	
        // Validar curso
        Curso curso = cursoService.buscarUno(requestDto.getIdCurso());
        if (curso == null) {
            return ResponseEntity.badRequest().body("Curso no válido");
        }

        // Crear la entidad PlanificacionCurso a partir del DTO
        PlanificacionCurso planificacion = PlanificacionCurso.builder()
                .diaSemana(requestDto.getDiaSemana())
                .horaInicio(requestDto.getHoraInicio())
                .horaFin(requestDto.getHoraFin())
                .curso(curso)
                .build();

        // Guardar en bbdd
        PlanificacionCurso nueva = planificacionService.insertUno(planificacion);
        if (nueva == null) {
            return ResponseEntity.badRequest().build();
        }

        // Respuesta a Dto
        PlanificacionCursoResponseDto response = modelMapper.map(nueva, PlanificacionCursoResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Actualizar planificación", description = "Actualiza los datos de una planificación existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Planificación actualizada correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanificacionCursoResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Curso no válido"),
        @ApiResponse(responseCode = "404", description = "Planificación no encontrada")
    })
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody PlanificacionCursoRequestDto requestDto) {
    	
    	// Validar planificacion
        PlanificacionCurso existente = planificacionService.buscarUno(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        // Validar Curso
        Curso curso = cursoService.buscarUno(requestDto.getIdCurso());
        if (curso == null) {
            return ResponseEntity.badRequest().build();
        }

        // Actualizar Planificacion
        existente.setDiaSemana(requestDto.getDiaSemana());
        existente.setHoraInicio(requestDto.getHoraInicio());
        existente.setHoraFin(requestDto.getHoraFin());
        existente.setCurso(curso);
        
        // Actualizar bbdd
        int result = planificacionService.updateUno(existente);
        return switch (result) {
            case 1 -> {
                PlanificacionCursoResponseDto dto = modelMapper.map(existente, PlanificacionCursoResponseDto.class);
                yield ResponseEntity.ok(dto); // Yield es como return dentro de un switch
            }
            case 0 -> ResponseEntity.notFound().build();
            case -1 -> ResponseEntity.badRequest().build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @Operation(summary = "Eliminar planificación", description = "Elimina una planificación por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Planificación eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Planificación no encontrada"),
        @ApiResponse(responseCode = "400", description = "Error en la petición")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
    	
        int result = planificacionService.deleteUno(id);
        return switch (result) {
            case 1 -> ResponseEntity.ok().build();
            case 0 -> ResponseEntity.notFound().build();
            case -1 -> ResponseEntity.badRequest().build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @Operation(summary = "Obtener planificaciones de un curso", description = "Devuelve todas las planificaciones asociadas a un curso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Planificaciones obtenidas correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanificacionCursoResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron planificaciones para este curso")
    })
    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<?> getByCurso(@PathVariable int idCurso) {
    	
    	// Validar planificaciones del Curso
        List<PlanificacionCurso> lista = planificacionService.buscarPorCurso(idCurso);
        if (lista == null || lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Convertir a Dto
        List<PlanificacionCursoResponseDto> response = lista.stream()
                .map(planificacion -> modelMapper.map(planificacion, PlanificacionCursoResponseDto.class))
                .toList();

        return ResponseEntity.ok(response);
    }
}
