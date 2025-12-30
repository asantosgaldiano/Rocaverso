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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import web.dto.EventRequestDto;
import web.dto.EventoResponseDto;
import web.modelo.entities.Destacado;
import web.modelo.entities.Estado;
import web.modelo.entities.Evento;
import web.modelo.entities.Rocodromo;
import web.modelo.entities.Tipo;
import web.modelo.services.EventoService;
import web.modelo.services.ReservaService;
import web.repository.ReservaRepository;
import web.repository.RocodromoRepository;
import web.repository.TipoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/evento")
@Tag(name = "Evento", description = "Operaciones relacionadas con la gestión de eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;
    
    @Autowired
    private ReservaService reservaService;
    
    @Autowired
    private TipoRepository tipoRepository;
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private RocodromoRepository rocodromoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Obtener todos los eventos", description = "Devuelve todos los eventos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de eventos obtenida correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventoResponseDto.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<List<EventoResponseDto>> getAll() {
    	
        List<Evento> eventos = eventoService.buscarTodos();
        
        // Convertir las entidades en DTOs
        List<EventoResponseDto> response = eventos.stream()
                .map(evento -> {
                    EventoResponseDto dto = modelMapper.map(evento, EventoResponseDto.class);
                    
                    // Tipo de evento
                    dto.setTipo(evento.getTipo().getNombre());
                    
                    // Calcular plazas libres
                    int reservasRealizadas = reservaRepository.countByEvento(evento);
                    int plazasLibres = evento.getAforoMaximo() - reservasRealizadas;
                    dto.setPlazasLibres(plazasLibres);
                    
                    return dto;
                }).toList();

        return ResponseEntity.ok(response);
    }
 
    @Operation(summary = "Obtener eventos activos", description = "Devuelve todos los eventos que se encuentran activos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de eventos activos obtenida correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventoResponseDto.class)))
    })
    @GetMapping("/activos")
    public ResponseEntity<List<EventoResponseDto>> getActivos() {
    	
        List<Evento> eventos = eventoService.buscarPorActivos();
        
        // Convertir las entidades en DTOs
        List<EventoResponseDto> response = eventos.stream()
                .map(evento -> {
                    EventoResponseDto dto = modelMapper.map(evento, EventoResponseDto.class);
                    
                    // Tipo de evento
                    dto.setTipo(evento.getTipo().getNombre());

                    // Calcular plazas libres
                    int reservasRealizadas = reservaRepository.countByEvento(evento);
                    int plazasLibres = evento.getAforoMaximo() - reservasRealizadas;
                    dto.setPlazasLibres(plazasLibres);

                    return dto;
                })
                .toList();
            return ResponseEntity.ok(response);
    }
 
    @Operation(summary = "Obtener detalle de un evento", description = "Devuelve los datos de un evento específico por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventoResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @GetMapping("/detail/{idEvento}")
    public ResponseEntity<?> getOne(@PathVariable int idEvento) {
    	
    	// Validar Evento
        Evento evento = eventoService.buscarUno(idEvento);
        if (evento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento no encontrado");
        }

        // Convertir la entidad a DTO
        EventoResponseDto dto = modelMapper.map(evento, EventoResponseDto.class);
        
        // Tipo de evento
        dto.setTipo(evento.getTipo().getNombre());
        
        // Calcular plazas libres
        int reservasRealizadas = reservaRepository.countByEvento(evento);
        int plazasLibres = evento.getAforoMaximo() - reservasRealizadas;
        dto.setPlazasLibres(plazasLibres);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Crear evento", description = "Permite crear un nuevo evento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evento creado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventoResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody EventRequestDto requestDto) {
    	
    	// Validar tipo de evento
        Tipo tipo = tipoRepository.findById(requestDto.getIdTipo()).orElse(null);
        if (tipo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipo no valido");
        }
        
        // Validar Rocodromo
        Rocodromo rocodromo = rocodromoRepository.findById(requestDto.getIdRocodromo()).orElse(null);
        if (rocodromo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rocodromo no valido");
        }
        
        // Crear entidad Evento
        Evento evento = Evento.builder()
                .nombre(requestDto.getNombre())
                .descripcion(requestDto.getDescripcion())
                .fechaInicio(requestDto.getFechaInicio())
                .horaInicio(requestDto.getHoraInicio())
                .horaFin(requestDto.getHoraFin())
                .estado(Estado.ACEPTADO) // fijo
                .destacado(Destacado.S)   // fijo
                .aforoMaximo(requestDto.getAforoMaximo())
                .minimoAsistencia(requestDto.getMinimoAsistencia())
                .precio(requestDto.getPrecio())
                .imagen(requestDto.getImagen())
                .tipo(tipo)
                .zona(requestDto.getZona())
                .rocodromo(rocodromo)
                .build();

        // Guardar en bbdd
        Evento nuevo = eventoService.insertUno(evento);
        if (nuevo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo crear el evento");
        }

        // Convertir la entidad a DTO de respuesta
        EventoResponseDto response = modelMapper.map(nuevo, EventoResponseDto.class);
        response.setTipo(nuevo.getTipo().getNombre());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Editar evento", description = "Permite actualizar un evento existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventoResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })    
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody EventRequestDto requestDto) {
    	
    	// Validar Evento
        Evento existente = eventoService.buscarUno(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento no encontrado");
        }

        // Validar tipo
        Tipo tipo = tipoRepository.findById(requestDto.getIdTipo()).orElse(null);
        if (tipo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipo no válido");
        }
        
        // Validar rocodromo
        Rocodromo rocodromo = rocodromoRepository.findById(requestDto.getIdRocodromo()).orElse(null);
        if (rocodromo == null) {
            return ResponseEntity.badRequest().body("Rocódromo no válido");
        }

        // Crear Evento Actualizado
        Evento eventoActualizado = Evento.builder()
                .idEvento(id)
                .nombre(requestDto.getNombre())
                .descripcion(requestDto.getDescripcion())
                .fechaInicio(requestDto.getFechaInicio())
                .horaInicio(requestDto.getHoraInicio())
                .horaFin(requestDto.getHoraFin())
                .estado(existente.getEstado()) // mantener estado
                .destacado(existente.getDestacado()) // mantener destacado
                .aforoMaximo(requestDto.getAforoMaximo())
                .minimoAsistencia(requestDto.getMinimoAsistencia())
                .precio(requestDto.getPrecio())
                .imagen(requestDto.getImagen())
                .tipo(tipo)
                .zona(requestDto.getZona())
                .rocodromo(rocodromo)
                .build();

        // Actualizar bbdd
        int result = eventoService.updateUno(eventoActualizado);
        if (result == 1) {
            EventoResponseDto response = modelMapper.map(eventoActualizado, EventoResponseDto.class);
            response.setTipo(tipo.getNombre());
            return ResponseEntity.ok(response);
        } else if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el evento");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en base de datos");
        }
    }

    @Operation(summary = "Cancelar evento", description = "Cancela un evento y elimina sus reservas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento cancelado correctamente"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error al eliminar reservas")
    })
    @PutMapping("/cancelar/{idEvento}")
    public ResponseEntity<?> cancelarEvento(@PathVariable int idEvento) {
    	
    	// Validar evento
        Evento evento = eventoService.buscarUno(idEvento);
        if (evento == null) {
            return ResponseEntity.notFound().build();
        }

        // Cambiar estado
        evento.setEstado(Estado.CANCELADO);

        // Actualizar bbdd
        int resultadoUpdate = eventoService.updateUno(evento);

        if (resultadoUpdate == 1) { 
        	// Si update es OK entonces, 
        	// Llamo al metodo elimar todas las reservas
            int resultadoDelete = reservaService.eliminarReservasEvento(idEvento); 
            // Casuisticas del metodo con switch
            return switch (resultadoDelete) {
                case 1 -> ResponseEntity.ok().build();
                case 2 -> ResponseEntity.ok().build();
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
    
    @Operation(summary = "Eliminar evento", description = "Elimina un evento por su ID")    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
    	
        int result = eventoService.deleteUno(id);
        return switch (result) {
            case 1 -> ResponseEntity.ok().build();
            case 0 -> ResponseEntity.notFound().build();
            case -1 -> ResponseEntity.badRequest().build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @Operation(summary = "Aceptar evento", description = "Cambia el estado del evento a ACEPTADO")
    @PutMapping("/aceptar/{idEvento}")
    public ResponseEntity<?> aceptarEvento(@PathVariable int idEvento) {
    	
    	// Validar Evento
        Evento evento = eventoService.buscarUno(idEvento);
        if (evento == null) {
            return ResponseEntity.notFound().build();
        }

        // Cambiar estado
        evento.setEstado(Estado.ACEPTADO);
        
        // Actualizar bbdd
        int resultadoUpdate = eventoService.updateUno(evento);
        return switch (resultadoUpdate) {
            case 1 -> ResponseEntity.ok().build();
            case 0 -> ResponseEntity.notFound().build();
            case -1 -> ResponseEntity.badRequest().build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @Operation(summary = "Terminar evento", description = "Cambia el estado del evento a TERMINADO")
    @PutMapping("/terminar/{idEvento}")
    public ResponseEntity<?> terminarEvento(@PathVariable int idEvento) {
    	
    	// Validar Evento
        Evento evento = eventoService.buscarUno(idEvento);
        if (evento == null) {
            return ResponseEntity.notFound().build();
        }

        //Cambiar estado
        evento.setEstado(Estado.TERMINADO);
        
        // Actualizar bbdd
        int resultadoUpdate = eventoService.updateUno(evento);
        return switch (resultadoUpdate) {
            case 1 -> ResponseEntity.ok().build();
            case 0 -> ResponseEntity.notFound().build();
            case -1 -> ResponseEntity.badRequest().build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }    
}
