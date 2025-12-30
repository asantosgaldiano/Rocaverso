package web.controller;

import java.util.List;

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

import web.dto.CursoRequestDto;
import web.dto.CursoResponseDto;
import web.dto.PlanificacionCursoRequestDto;
import web.modelo.entities.Curso;
import web.modelo.entities.Estado;
import web.modelo.entities.PlanificacionCurso;
import web.modelo.entities.Rocodromo;
import web.modelo.entities.Rol;
import web.modelo.entities.Usuario;
import web.modelo.services.CursoService;
import web.modelo.services.InscripcionService;
import web.modelo.services.PlanificacionCursoService;
import web.repository.InscripcionRepository;
import web.repository.RocodromoRepository;
import web.repository.UsuarioRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/curso")
@Tag(name = "Curso", description = "Operaciones relacionadas con la gestión de cursos")
public class CursoController {
	
    @Autowired
    private CursoService cursoService;
    
    @Autowired
    private InscripcionService inscripcionService;
    
    @Autowired
    private PlanificacionCursoService planificacionCursoService;
    
    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private RocodromoRepository rocodromoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Obtener todos los cursos", description = "Devuelve la lista completa de cursos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cursos obtenida correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponseDto.class))
        )
    })
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
    	
        List<Curso> cursos = cursoService.buscarTodos();
        
        List<CursoResponseDto> response = cursos.stream()
            .map(curso -> {
                CursoResponseDto dto = modelMapper.map(curso, CursoResponseDto.class);
                
                // Añado nombre completo monitor al dto
                if (curso.getMonitor() != null) {
                    dto.setNombreMonitor(curso.getMonitor().getNombre() + " " + curso.getMonitor().getApellidos());
                }
                
                // Calculo Plazas libres
                int inscripcionesRealizadas = inscripcionRepository.countByCurso(curso);
                int plazasLibres = curso.getAforoMaximo() - inscripcionesRealizadas;
                dto.setPlazasLibres(plazasLibres);
                
                return dto;
            }).toList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener cursos activos", description = "Devuelve la lista de cursos que se encuentran activos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cursos activos obtenida correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponseDto.class))
        )
    })
    @GetMapping("/activos")
    public ResponseEntity<?> getActivos() {
    	
        List<Curso> cursos = cursoService.buscarPorActivos();
        
        List<CursoResponseDto> response = cursos.stream()
            .map(curso -> {
                CursoResponseDto dto = modelMapper.map(curso, CursoResponseDto.class);
                
                // Añado nombre completo monitor al dto
                if (curso.getMonitor() != null) {
                    dto.setNombreMonitor(curso.getMonitor().getNombre() + " " + curso.getMonitor().getApellidos());
                }
                
                // Calculo Plazas libres                
                int inscripcionesRealizadas = inscripcionRepository.countByCurso(curso);
                int plazasLibres = curso.getAforoMaximo() - inscripcionesRealizadas;
                dto.setPlazasLibres(plazasLibres);
                
                return dto;
            }).toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener detalle de un curso", description = "Devuelve la información de un curso específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponseDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @GetMapping("/detail/{idCurso}")
    public ResponseEntity<?> getOne(@PathVariable int idCurso) {
    	
        Curso curso = cursoService.buscarUno(idCurso);
        if (curso == null) {
            return ResponseEntity.notFound().build(); // NO EXISTE
        }

        CursoResponseDto dto = modelMapper.map(curso, CursoResponseDto.class);
        
        int inscripcionesRealizadas = inscripcionRepository.countByCurso(curso);
        int plazasLibres = curso.getAforoMaximo() - inscripcionesRealizadas;
        dto.setPlazasLibres(plazasLibres);
        
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Crear curso", description = "Permite crear un nuevo curso con planificaciones y monitor asignado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Curso creado correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CursoResponseDto.class),
                examples = @ExampleObject(
                    name = "Curso creado",
                    value = "{\n" +
                            "  \"idCurso\": 1,\n" +
                            "  \"nombre\": \"Curso de Iniciación\",\n" +
                            "  \"descripcion\": \"Aprender los fundamentos de la escalada\",\n" +
                            "  \"fechaInicio\": \"2025-11-01\",\n" +
                            "  \"fechaFin\": \"2025-12-01\",\n" +
                            "  \"aforoMaximo\": 20,\n" +
                            "  \"minimoAsistencia\": 5,\n" +
                            "  \"precio\": 50.0,\n" +
                            "  \"nombreMonitor\": \"Juan Pérez\",\n" +
                            "  \"plazasLibres\": 20\n" +
                            "}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario/monitor/rocodromo no encontrados"),
        @ApiResponse(responseCode = "409", description = "Conflictos de horario del monitor")
    })
    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody CursoRequestDto requestDto) {
    	
        // Obtener email usuario autenticado desde token JWT (SecurityContext)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailToken = auth.getName();

        // Validar usuario logueado se ADMIN
        Usuario usuarioLogueado = usuarioRepository.findById(emailToken).orElse(null);
        if (usuarioLogueado == null || usuarioLogueado.getTipoRol() != Rol.ADMON) {
            return ResponseEntity.badRequest().build();
        }
    	
        // Validar rocodromo
    	Rocodromo rocodromo = rocodromoRepository.findById(requestDto.getIdRocodromo()).orElse(null);
        if (rocodromo == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // Validar monitor por email y rol MONITOR
        Usuario monitor = usuarioRepository.findById(requestDto.getEmailMonitor()).orElse(null);
        if (monitor == null || monitor.getTipoRol() != Rol.MONITOR) {
            return ResponseEntity.badRequest().build();
        }
        
        // Comprobar conflictos de horario del monitor
        if (requestDto.getPlanificaciones() != null && !requestDto.getPlanificaciones().isEmpty()) {
            boolean hayConflicto = cursoService.tieneConflictosDeHorario(
                requestDto.getEmailMonitor(), 
                0,  // 0 porque es un curso nuevo
                requestDto.getPlanificaciones()
            );
            if (hayConflicto) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        // Crear objero Curso
        Curso curso = Curso.builder()
                .nombre(requestDto.getNombre())
                .descripcion(requestDto.getDescripcion())
                .fechaInicio(requestDto.getFechaInicio())
                .fechaFin(requestDto.getFechaFin())
                .estado(Estado.ACEPTADO)
                .aforoMaximo(requestDto.getAforoMaximo())
                .minimoAsistencia(requestDto.getMinimoAsistencia())
                .precio(requestDto.getPrecio())
                .zona(requestDto.getZona())
                .rocodromo(rocodromo)
                .monitor(monitor)
                .build();

        // Agregar las planificaciones del curso
        if (requestDto.getPlanificaciones() != null) {
            List<PlanificacionCurso> planificaciones = requestDto.getPlanificaciones().stream()
                .map(dto -> {
                    PlanificacionCurso p = modelMapper.map(dto, PlanificacionCurso.class);
                    p.setCurso(curso);
                    return p;
                }).toList();
            curso.setPlanificaciones(planificaciones);
        }

        // Guardar curso en bbdd
        Curso nuevo = cursoService.insertUno(curso);
        if (nuevo == null) {
            return ResponseEntity.badRequest().build();
        }

        // Crear respuesta Dto
        CursoResponseDto response = modelMapper.map(nuevo, CursoResponseDto.class);
        response.setNombreMonitor(monitor.getNombre() + " " + monitor.getApellidos());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @Operation(summary = "Editar curso", description = "Permite actualizar los datos de un curso existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponseDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflictos de horario del monitor")
    })
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody CursoRequestDto requestDto) {
    	
    	// Validar curso
        Curso existente = cursoService.buscarUno(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        // Validar rocodromo
        Rocodromo rocodromo = rocodromoRepository.findById(requestDto.getIdRocodromo()).orElse(null);
        if (rocodromo == null) {
            return ResponseEntity.badRequest().build();
        }

        // Comprobar conflictos de horario de monitor
        if (requestDto.getPlanificaciones() != null && !requestDto.getPlanificaciones().isEmpty()) {
            boolean hayConflicto = cursoService.tieneConflictosDeHorario(
                existente.getMonitor().getEmail(),  // monitor actual
                id,  // id del curso que edito
                requestDto.getPlanificaciones()
            );
            if (hayConflicto) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        
        // Validar monitor
        Usuario monitor = usuarioRepository.findById(requestDto.getEmailMonitor()).orElse(null);
        if (monitor == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // Crear objeto curso
        Curso cursoActualizado = Curso.builder()
                .idCurso(id)
                .nombre(requestDto.getNombre())
                .descripcion(requestDto.getDescripcion())
                .fechaInicio(requestDto.getFechaInicio())
                .fechaFin(requestDto.getFechaFin())
                .estado(existente.getEstado())
                .aforoMaximo(requestDto.getAforoMaximo())
                .minimoAsistencia(requestDto.getMinimoAsistencia())
                .precio(requestDto.getPrecio())
                .zona(requestDto.getZona())
                .rocodromo(rocodromo)
                .monitor(monitor)
                .build();

        // Actualizar planificaciones
        if (requestDto.getPlanificaciones() != null) {
            List<PlanificacionCurso> planificaciones = requestDto.getPlanificaciones().stream()
                .map(dto -> {
                    PlanificacionCurso p = modelMapper.map(dto, PlanificacionCurso.class);
                    p.setCurso(cursoActualizado);
                    return p;
                }).toList();
            cursoActualizado.setPlanificaciones(planificaciones);
        }

        // Guardar cambios
        int result = cursoService.updateUno(cursoActualizado);
        if (result == 1) {
            CursoResponseDto response = modelMapper.map(cursoActualizado, CursoResponseDto.class);
            return ResponseEntity.ok(response);
        } else if (result == 0) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Agregar planificaciones a un curso", description = "Permite agregar nuevas planificaciones a un curso existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Planificaciones agregadas correctamente"),
        @ApiResponse(responseCode = "400", description = "Lista de planificaciones vacía o inválida"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflictos de horario del monitor")
    })
    @PostMapping("/addplanificacioncurso/{idCurso}")
    public ResponseEntity<?> addPlanificacionCurso(
            @PathVariable int idCurso,
            @RequestBody List<PlanificacionCursoRequestDto> planificacionesDto) {
        
        // Verificar si el curso existe
        Curso curso = cursoService.buscarUno(idCurso);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificar si hay planificaciones
        if (planificacionesDto == null || planificacionesDto.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Comprobar conflictos de horario con el monitor
        String emailMonitor = curso.getMonitor().getEmail();
        boolean hayConflicto = cursoService.tieneConflictosDeHorario(
            emailMonitor,
            idCurso, 
            planificacionesDto
        );
        if (hayConflicto) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Convertir los dto a entidades
        List<PlanificacionCurso> nuevas = planificacionesDto.stream()
            .map(dto -> {
                PlanificacionCurso plan = modelMapper.map(dto, PlanificacionCurso.class);
                plan.setCurso(curso);
                return plan;
            }).toList();
        
        // Guardar planificaciones
        for (PlanificacionCurso plan : nuevas) {
            planificacionCursoService.insertUno(plan);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Cancelar curso", description = "Permite cancelar un curso existente y elimina las inscripciones asociadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso cancelado correctamente"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno al cancelar curso o eliminar inscripciones")
    })
    @PutMapping("/cancelar/{idCurso}")
    public ResponseEntity<?> cancelarCurso(@PathVariable int idCurso) {
    	
    	// Validar curso
        Curso curso = cursoService.buscarUno(idCurso);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        // Cambiar estado
        curso.setEstado(Estado.CANCELADO);
        
        // Guardar cambios en bbdd
        int resultadoUpdate = cursoService.updateUno(curso);

        if (resultadoUpdate == 1) {
        	// Si es ok, elimino inscripciones al curso
            int resultadoDelete = inscripcionService.eliminarInscripcionesCurso(idCurso);

            return switch (resultadoDelete) {
                case 1, 2 -> ResponseEntity.ok().build();
                case -1 -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            };

        } else if (resultadoUpdate == 0) {
            return ResponseEntity.notFound().build();
        } else if (resultadoUpdate == -1) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar curso", description = "Permite eliminar un curso existente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "400", description = "Error en la petición"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        int result = cursoService.deleteUno(id);
        return switch (result) {
	        case 1 -> ResponseEntity.ok().build();
	        case 0 -> ResponseEntity.notFound().build();
	        case -1 -> ResponseEntity.badRequest().build();
	        default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    };
    }

    @Operation(summary = "Obtener cursos del monitor", description = "Devuelve la lista de cursos que pertenecen al monitor autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cursos del monitor obtenida correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponseDto.class))),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado o no es monitor")
    })
    @GetMapping("/miscursos")
    public ResponseEntity<?> getCursosDelMonitor() {
        
    	// Obtener email usuario autenticado desde token JWT (SecurityContext)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailMonitor = auth.getName();

        // Verificar que el usuario exista y sea un monitor
        Usuario usuario = usuarioRepository.findById(emailMonitor).orElse(null);
        if (usuario == null || usuario.getTipoRol() != Rol.MONITOR) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Buscar los cursos del monitor
        List<Curso> cursos = cursoService.buscarPorMonitor(emailMonitor);
        
        // Convertir los cursos a DTOs de respuesta
        List<CursoResponseDto> response = cursos.stream()
            .map(curso -> {
                CursoResponseDto dto = modelMapper.map(curso, CursoResponseDto.class);
                dto.setNombreMonitor(usuario.getNombre() + " " + usuario.getApellidos());
                return dto;
            }).toList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Aceptar curso", description = "Cambia el estado del curso a ACEPTADO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso aceptado correctamente"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "400", description = "Error al actualizar"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/aceptar/{idCurso}")
    public ResponseEntity<?> aceptarCurso(@PathVariable int idCurso) {
    	
    	// Validar curso
        Curso curso = cursoService.buscarUno(idCurso);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        // Cambiar estado
        curso.setEstado(Estado.ACEPTADO);
        
        // Actualizar bbdd
        int resultadoUpdate = cursoService.updateUno(curso);

        return switch (resultadoUpdate) {
            case 1 -> ResponseEntity.ok().build();
            case 0 -> ResponseEntity.notFound().build();
            case -1 -> ResponseEntity.badRequest().build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @Operation(summary = "Terminar curso", description = "Cambia el estado del curso a TERMINADO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso terminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "400", description = "Error al actualizar"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/terminar/{idCurso}")
    public ResponseEntity<?> terminarCurso(@PathVariable int idCurso) {
    	
    	// Validar curso
        Curso curso = cursoService.buscarUno(idCurso);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Cambiar estado
        curso.setEstado(Estado.TERMINADO);
        
        // Actualizar bbdd        
        int resultadoUpdate = cursoService.updateUno(curso);

        return switch (resultadoUpdate) {
            case 1 -> ResponseEntity.ok().build();
            case 0 -> ResponseEntity.notFound().build();
            case -1 -> ResponseEntity.badRequest().build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @Operation(
    	    summary = "Obtener cursos de un monitor por email",
    	    description = "Devuelve todos los cursos que imparte un monitor específico, independientemente del usuario autenticado."
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Cursos obtenidos correctamente",
    	        content = @Content(mediaType = "application/json",
    	        schema = @Schema(implementation = CursoResponseDto.class))),

    	    @ApiResponse(responseCode = "404", description = "Monitor no encontrado"),
    	})
	@GetMapping("/monitor/{emailMonitor}")
	public ResponseEntity<?> getCursosPorMonitor(@PathVariable String emailMonitor) {

	    Usuario monitor = usuarioRepository.findById(emailMonitor).orElse(null);

	    // Validar existencia y rol
	    if (monitor == null || monitor.getTipoRol() != Rol.MONITOR) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("El monitor no existe o no es un monitor.");
	    }

	    List<Curso> cursos = cursoService.buscarPorMonitor(emailMonitor);

	    List<CursoResponseDto> response = cursos.stream()
	        .map(curso -> {
	            CursoResponseDto dto = modelMapper.map(curso, CursoResponseDto.class);
	            dto.setNombreMonitor(monitor.getNombre() + " " + monitor.getApellidos());
	            return dto;
	        }).toList();

	    return ResponseEntity.ok(response);
	}
    
    
}
