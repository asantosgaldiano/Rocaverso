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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import web.dto.ViaRequestDto;
import web.dto.ViaResponseDto;
import web.modelo.entities.Dificultad;
import web.modelo.entities.EstadoVia;
import web.modelo.entities.Rocodromo;
import web.modelo.entities.TipoVia;
import web.modelo.entities.Via;
import web.modelo.entities.Zona;
import web.modelo.services.ViaService;
import web.repository.RocodromoRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/via")
@Tag(name = "Vías", description = "Operaciones relacionadas con la gestión de vías de escalada")
public class ViaController {

    @Autowired
    private ViaService viaService;
    
    @Autowired
    private RocodromoRepository rocodromoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Obtener todas las vías", description = "Devuelve la lista completa de vías del rocódromo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de vías obtenida correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViaResponseDto.class))
        )
    })
    @GetMapping("/all")
    public ResponseEntity<List<ViaResponseDto>> getAll() {
    	
        List<Via> vias = viaService.buscarTodos();
        
        List<ViaResponseDto> response = vias.stream()
            .map(via -> modelMapper.map(via, ViaResponseDto.class))
            .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener vías activas", description = "Devuelve la lista de vías que se encuentran activas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de vías activas obtenida correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViaResponseDto.class))
        )
    })
    @GetMapping("/activas")
    public ResponseEntity<List<ViaResponseDto>> getViasActivas() {
    	
        List<Via> vias = viaService.buscarPorEstado(EstadoVia.ACTIVA);
        
        List<ViaResponseDto> response = vias.stream()
            .map(via -> modelMapper.map(via, ViaResponseDto.class))
            .toList();

        return ResponseEntity.ok(response);    
    }

    @Operation(summary = "Obtener detalle de una vía", description = "Devuelve la información de una vía específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vía encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViaResponseDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "Vía no encontrada")
    })
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getOne(@PathVariable int id) {
    	
        Via via = viaService.buscarUno(id);
        if (via == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vía no encontrada");
        }

        ViaResponseDto dto = modelMapper.map(via, ViaResponseDto.class);
        
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Crear vía", description = "Permite crear una nueva vía asociada a un rocódromo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Vía creada correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViaResponseDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "Rocódromo inválido o error al crear la vía")
    })
    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody ViaRequestDto requestDto) {
    	
    	// Validar Rocodromo
        Rocodromo rocodromo = rocodromoRepository.findById(requestDto.getIdRocodromo()).orElse(null);
        if (rocodromo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rocódromo no válido");
        }

        // Crear entidad Via
        Via via = Via.builder()
                .dificultad(requestDto.getDificultad())
                .tipo(requestDto.getTipo())
                .estado(EstadoVia.ACTIVA) // por defecto
                .ubicacion(requestDto.getUbicacion())
                .rocodromo(rocodromo)
                .build();

        // Guardar en bbdd
        Via insertada = viaService.insertUno(via);
        if (insertada == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo crear la vía");
        }

        // Convertir dto respuesta
        ViaResponseDto response = modelMapper.map(insertada, ViaResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Editar vía", description = "Permite actualizar los datos de una vía existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vía actualizada correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViaResponseDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o error al actualizar"),
        @ApiResponse(responseCode = "404", description = "Vía no encontrada")
    })
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody ViaRequestDto requestDto) {
    	
    	// Validar Vía
        Via existente = viaService.buscarUno(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vía no encontrada");
        }

        // Validar rocodromo
        Rocodromo rocodromo = rocodromoRepository.findById(requestDto.getIdRocodromo()).orElse(null);
        if (rocodromo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rocódromo no válido");
        }

        // Crear entidad Actualizada
        Via viaActualizada = Via.builder()
                .idVia(id)
                .dificultad(requestDto.getDificultad())
                .tipo(requestDto.getTipo())
                .estado(requestDto.getEstado())
                .ubicacion(requestDto.getUbicacion())
                .rocodromo(rocodromo)
                .build();

        // Actualizar bbdd
        int result = viaService.updateUno(viaActualizada);
        if (result == 1) {
            ViaResponseDto response = modelMapper.map(viaActualizada, ViaResponseDto.class);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar la vía");
        }
    }

    @Operation(summary = "Eliminar vía", description = "Permite eliminar una vía existente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vía eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Vía no encontrada"),
        @ApiResponse(responseCode = "400", description = "Error en la petición")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
    	
        int result = viaService.deleteUno(id);
        return switch (result) {
            case 1 -> ResponseEntity.ok().build();
            case 0 -> ResponseEntity.notFound().build();
            case -1 -> ResponseEntity.badRequest().build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @Operation(summary = "Activar vía", description = "Cambia el estado de una vía a ACTIVA")
    @PutMapping("/activar/{id}")
    public ResponseEntity<?> activar(@PathVariable int id) {
    	
        Via via = viaService.buscarUno(id);
        if (via == null) {
            return ResponseEntity.notFound().build();
        }
        
        via.setEstado(EstadoVia.ACTIVA);
        
        return viaService.updateUno(via) == 1 ?
        		ResponseEntity.ok().build() :
        			ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Desactivar vía", description = "Cambia el estado de una vía a INACTIVA")
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<?> desactivar(@PathVariable int id) {
    	
        Via via = viaService.buscarUno(id);
        if (via == null) {
            return ResponseEntity.notFound().build();
        }
        
        via.setEstado(EstadoVia.INACTIVA);
        
        return viaService.updateUno(via) == 1 ?
        		ResponseEntity.ok().build() :
        			ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Obtener tipos de vía", description = "Devuelve todos los valores posibles del enum TipoVia")    
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> getByTipo(@PathVariable String tipo) {
        try {
        	// Convertir String a Enum
            TipoVia tipoVia = TipoVia.valueOf(tipo.toUpperCase());
            
            // Buscar Vias por tipo
            List<Via> vias = viaService.buscarPorTipo(tipoVia);
            if (vias.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            // Crear Dto respuesta
            List<ViaResponseDto> response = vias.stream()
                .map(v -> modelMapper.map(v, ViaResponseDto.class))
                .toList();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }    

    @Operation(summary = "Obtener dificultades", description = "Devuelve todos los valores posibles del enum Dificultad")
    @GetMapping("/dificultad/{dificultad}")
    public ResponseEntity<?> getByDificultad(@PathVariable String dificultad) {
        try {
        	// Convertir String a Enum
            Dificultad dificultadEnum = Dificultad.valueOf(dificultad.toUpperCase());
            
            // Buscar Vias por dificultad
            List<Via> vias = viaService.buscarPorDificultad(dificultadEnum);
            if (vias.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Crear dto respuesta
            List<ViaResponseDto> response = vias.stream()
                .map(v -> modelMapper.map(v, ViaResponseDto.class))
                .toList();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtener ubicaciones", description = "Devuelve todos los valores posibles del enum Zona")
    @GetMapping("/ubicacion/{ubicacion}")
    public ResponseEntity<?> getByUbicacion(@PathVariable String ubicacion) {
        try {
        	// Convertir String a Enum
            Zona zona = Zona.valueOf(ubicacion.toUpperCase());
            
            // Buscar vias por ubicacion
            List<Via> vias = viaService.buscarPorUbicacion(zona);
            if (vias.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Crear Dto respuesta
            List<ViaResponseDto> response = vias.stream()
                .map(v -> modelMapper.map(v, ViaResponseDto.class))
                .toList();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Filtrar vías", description = "Permite filtrar vías por tipo, dificultad y ubicación")
    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrarVias(
        @RequestParam(required = false) String tipo,
        @RequestParam(required = false) String dificultad,
        @RequestParam(required = false) String ubicacion
    ) {
        try {
        	// Convertir strings a enums
        	// Si no paso parametro, se mantiene null y no se filtrara por ese campo
            TipoVia tipoVia = (tipo != null) ? TipoVia.valueOf(tipo.toUpperCase()) : null;
            Dificultad dificultadEnum = (dificultad != null) ? Dificultad.valueOf(dificultad.toUpperCase()) : null;
            Zona zona = (ubicacion != null) ? Zona.valueOf(ubicacion.toUpperCase()) : null;

            // Buscar filtrado
            List<Via> resultado = viaService.buscarFiltrado(tipoVia, dificultadEnum, zona);
            if (resultado.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Crear dto respuesta
            List<ViaResponseDto> response = resultado.stream()
                .map(v -> modelMapper.map(v, ViaResponseDto.class))
                .toList();

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GETs Para obtener todos los valores de los ENUMs
    
    @GetMapping("/tipos")
    public TipoVia[] getTipos() {
        return TipoVia.values();
    }

    @GetMapping("/dificultades")
    public Dificultad[] getDificultades() {
        return Dificultad.values();
    }

    @GetMapping("/ubicaciones")
    public Zona[] getUbicaciones() {
        return Zona.values();
    }
    
}
