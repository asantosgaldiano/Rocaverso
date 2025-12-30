package web.controller;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.dto.UsuarioTopResponseDto;
import web.dto.ViaRealizadaRequestDto;
import web.dto.ViaRealizadaResponseDto;
import web.dto.ViaTopResponseDto;
import web.modelo.entities.Usuario;
import web.modelo.entities.Via;
import web.modelo.entities.ViaRealizada;
import web.modelo.services.UsuarioService;
import web.modelo.services.ViaRealizadaService;
import web.modelo.services.ViaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/viarealizada")
@Tag(name = "Vías Realizadas", description = "Operaciones relacionadas con las vías realizadas por los usuarios")
public class ViaRealizadaController {
	
    @Autowired
    private ViaRealizadaService viaRealizadaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ViaService viaService;

    @Autowired
    private ModelMapper modelMapper;
 
    @Operation(summary = "Obtener vías realizadas del usuario autenticado", description = "Devuelve la lista de vías realizadas asociadas al usuario actualmente autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vías obtenida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViaRealizadaResponseDto.class))
            )
    })
    @GetMapping("/misvias")
    public ResponseEntity<List<ViaRealizadaResponseDto>> obtenerViasRealizadasPorUsuario() {
    	
    	// Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Buscar por usuario
        List<ViaRealizada> viasRealizadas = viaRealizadaService.buscarPorEmailUsuario(email);

        // Crear dto respuesta
        List<ViaRealizadaResponseDto> response = viasRealizadas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

            return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar nueva vía realizada", description = "Permite al usuario autenticado registrar una nueva vía realizada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vía realizada registrada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ViaRealizadaResponseDto.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"idViaRealizada\": 1,\n" +
                                            "  \"idVia\": 3,\n" +
                                            "  \"tipo\": \"Bloque\",\n" +
                                            "  \"dificultad\": \"Intermedio\",\n" +
                                            "  \"nombreUsuario\": \"Ana López\",\n" +
                                            "  \"fechaRealizacion\": \"2025-10-24\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Vía o usuario no válido")
    })
    @PostMapping("/add")
    public ResponseEntity<?> crearViaRealizada(@RequestBody ViaRealizadaRequestDto request) {
    	
    	// Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailAuth = auth.getName();

        // Validar Via y Usuario
        Usuario usuario = usuarioService.buscarPorEmail(emailAuth);
        Via via = viaService.buscarUno(request.getIdVia());
        if (usuario == null || via == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vía o usuario no válido");
        }

        // Crear ViaRealizada
        ViaRealizada viaRealizada = ViaRealizada.builder()
            .via(via)
            .usuario(usuario)
            .fechaRealizacion(new Date(System.currentTimeMillis()))
            .build();

        // Guardar bbdd
        ViaRealizada creada = viaRealizadaService.insertUno(viaRealizada);
        if (creada == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo registrar la vía realizada");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(creada));
    }

    @Operation(summary = "Eliminar vía realizada", description = "Permite al usuario autenticado eliminar una vía realizada propia por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vía realizada eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Vía realizada no encontrada"),
            @ApiResponse(responseCode = "401", description = "No autorizado para eliminar esta vía")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminarViaRealizada(@PathVariable int id) {
    	
    	// Usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Buscar por usuario
        ViaRealizada viaRealizada = viaRealizadaService.buscarUno(id);
        if (viaRealizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la vía realizada");
        }

        // Validar propietario
        if (!viaRealizada.getUsuario().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado para eliminar esta vía realizada");
        }

        // Borrar en bbdd
        int result = viaRealizadaService.deleteUno(id);
        return result == 1
            ? ResponseEntity.ok("Vía realizada eliminada")
            : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar");
    }


    @Operation(summary = "Actualizar vía realizada", description = "Permite al usuario autenticado actualizar una vía realizada propia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vía realizada actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViaRealizadaResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Vía realizada no encontrada"),
            @ApiResponse(responseCode = "401", description = "No autorizado para modificar esta vía"),
            @ApiResponse(responseCode = "400", description = "Vía no válida"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar la vía realizada")
    })
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> actualizarViaRealizada(@PathVariable int id, @RequestBody ViaRealizadaRequestDto request) {
    	
    	// Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailAuth = auth.getName();

        // Buscar viarealizada
        ViaRealizada existente = viaRealizadaService.buscarUno(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la vía realizada");
        }
        
        // Validar propietario
        if (!existente.getUsuario().getEmail().equals(emailAuth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado para modificar esta vía realizada");
        }

        // Validar Nuevavia
        Via nuevaVia = viaService.buscarUno(request.getIdVia());
        if (nuevaVia == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vía no válida");
        }

        // Actualizar la vía y la fecha a la fecha actual
        existente.setVia(nuevaVia);
        existente.setFechaRealizacion(new Date(System.currentTimeMillis()));

        // Actualizar bbdd
        int resultado = viaRealizadaService.updateUno(existente);
        if (resultado == 1) {
            // Devolver el dto actualizado
            ViaRealizada actualizado = viaRealizadaService.buscarUno(id);
            return ResponseEntity.ok(convertToDto(actualizado));
        } else if (resultado == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la vía realizada para actualizar");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la vía realizada");
        }
    }

    @Operation(summary = "Obtener top 5 vías más realizadas", description = "Devuelve las 5 vías más realizadas por todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top 5 vías obtenido correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViaTopResponseDto.class))
            )
    })
    @GetMapping("/top5")
    public ResponseEntity<List<ViaTopResponseDto>> obtenerTop5Vias() {
 
    	// Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        // Buscar ViasRealizdas
        // Como es una consulta personalizada, Spring Data devuelvo Object[], 
        // donde cada elemento del array corresponde a una columna de la fila
        // cada fila de la consulta tiene dos columnas
        List<Object[]> resultados = viaRealizadaService.topViasRealizadas();

        // Crear Dto respuesta
        List<ViaTopResponseDto> topVias = resultados.stream()
                .map(obj -> { // es un Object[] que contiene la fila [id_via, total].
                	
                    int idVia = (int) obj[0]; // casting a int
                    long total = (long) obj[1]; // casting a long, (porque COUNT(*) devuelve long en SQL)

                    Via via = viaService.buscarUno(idVia);

                    // Creo Dto respuesta
                    return ViaTopResponseDto.builder()
                            .idVia(via.getIdVia())
                            .tipo(via.getTipo())
                            .dificultad(via.getDificultad())
                            .ubicacion(via.getUbicacion())
                            .totalRealizaciones(total)
                            .build();
                })
                .limit(5) // Solo me quedo con las 5 primeras
                .collect(Collectors.toList()); // convierte el stream de DTOs en una lista final.

        return ResponseEntity.ok(topVias);
    }

    @Operation(summary = "Obtener top 3 usuarios con más vías realizadas", description = "Devuelve los 3 usuarios que más vías han realizado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top 3 usuarios obtenido correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioTopResponseDto.class))
            )
    })
    @GetMapping("/podio3")
    public ResponseEntity<List<UsuarioTopResponseDto>> obtenerTop3Usuarios() {
    	
    	// Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Consulta personalizada por eso devuelvo object
        List<Object[]> resultados = viaRealizadaService.topUsuarios();

     // Crear Dto respuesta
        List<UsuarioTopResponseDto> topUsuarios = resultados.stream()
                .limit(3) // Solo los 3 primeros
                .map(obj -> {
                    String email = (String) obj[0]; // casting a string
                    Long total = (Long) obj[1]; // casting a long, (porque COUNT(*) devuelve long en SQL)
                    
                    Usuario usuario = usuarioService.buscarUno(email);
                    return UsuarioTopResponseDto.builder()
                            .email(usuario.getEmail())
                            .nombre(usuario.getNombre())
                            .apellidos(usuario.getApellidos())
                            .totalVias(total)
                            .build();
                })
                .collect(Collectors.toList()); // convierte el stream de DTOs en una lista final.

        return ResponseEntity.ok(topUsuarios);
    }

    // Método privado para convertir entidad a DTO
    private ViaRealizadaResponseDto convertToDto(ViaRealizada entity) {
        return ViaRealizadaResponseDto.builder()
                .idViaRealizada(entity.getIdViaRealizada())
                .idVia(entity.getVia().getIdVia())
                .tipo(entity.getVia().getTipo())
                .dificultad(entity.getVia().getDificultad())
                .nombreUsuario(entity.getUsuario().getNombre())
                .fechaRealizacion(entity.getFechaRealizacion())
                .build();
    }
    
}
