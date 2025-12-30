package web.controller;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.dto.EstadisticasUsuarioDto;
import web.dto.MisInscripcionesCalendarDto;
import web.dto.MisInscripcionesResponseDto;
import web.dto.MisReservasResponseDto;
import web.dto.UsuarioRequestDto;
import web.dto.UsuarioResponseDto;
import web.modelo.entities.Usuario;
import web.modelo.services.EstadisticasService;
import web.modelo.services.InscripcionService;
import web.modelo.services.ReservaService;
import web.modelo.services.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuario")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios, reservas e inscripciones")
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	ReservaService reservaService;
	
	@Autowired
	EstadisticasService estadisticasService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private InscripcionService inscripcionService;
	
	@Autowired
	private ModelMapper modelMapper;

    @Operation(summary = "Listar todos los usuarios", description = "Devuelve la lista completa de usuarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios obtenidos correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "No se encontraron usuarios")
    })
	@GetMapping("/all")
	public ResponseEntity<List<UsuarioResponseDto>> todos() {
		
		// Obtener todos los usuarios
		List<Usuario> usuarios = usuarioService.buscarTodos();
		
		// Convertir a Dto respuesta
		List<UsuarioResponseDto> response = usuarios.stream()
	            .map(usuario -> UsuarioResponseDto.builder()
	                    .email(usuario.getEmail())
	                    .nombre(usuario.getNombre())
	                    .apellidos(usuario.getApellidos())
	                    .fechaRegistro(usuario.getFechaRegistro())
	                    .rol(usuario.getRol().name())
	                    .enabled(usuario.getEnabled())
	                    .build())
	            .toList();

        return ResponseEntity.ok(response);
	}

    @Operation(summary = "Obtener detalle de un usuario", description = "Devuelve la información de un usuario específico por su email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/detail/{email}")
	public ResponseEntity<?> uno(@PathVariable String email) {
		
		// Validar usuario
		Usuario usuario = usuarioService.buscarUno(email);
		if (usuario == null) {
			return new ResponseEntity<String>("Este usuario no existe", HttpStatus.NOT_FOUND);

		}		
		
		// Convertir a dto respuesta
		UsuarioResponseDto response = modelMapper.map(usuario, UsuarioResponseDto.class);
		
		return new ResponseEntity<UsuarioResponseDto>(response, HttpStatus.OK);
	}

    @Operation(summary = "Editar usuario", description = "Permite actualizar los datos de un usuario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Error en la actualización"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
	@PutMapping("/edit/{email}")
	public ResponseEntity<?> modificar(@PathVariable String email, @RequestBody UsuarioRequestDto usuarioRequest) {
		
		// Validar Usuario
		Usuario usuarioExistente = usuarioService.buscarUno(email);		
		if (usuarioExistente == null) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay coincidencia con el usuario");

		}
		
	    // Convertir el DTO a entidad Usuario 
	    Usuario usuarioActualizado = Usuario.builder()
	            .email(email) // Aseguro que no cambia
	            .nombre(usuarioRequest.getNombre())
	            .apellidos(usuarioRequest.getApellidos())
	            .password(usuarioRequest.getPassword())
	            .fechaRegistro(usuarioExistente.getFechaRegistro()) // mantengo fecha de registro
	            .rol(usuarioExistente.getRol()) // mantengo rol
	            .enabled(usuarioExistente.getEnabled()) // mantengo estado
	            .build();

	    // Actualizar bbdd
		int response = usuarioService.updateUno(usuarioActualizado);
		switch(response) {
			case 1:  return new ResponseEntity<>("Usuario actualizado correctamente", HttpStatus.OK);
			case 0:  return new ResponseEntity<>("Usuario no existe", HttpStatus.NOT_FOUND);
			case -1: return new ResponseEntity<>("Esto es un problema de la base de datos, llame a servicio Tecnico", HttpStatus.BAD_REQUEST);
			default:  return new ResponseEntity<>("Error desconocido", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	

    @Operation(summary = "Eliminar usuario", description = "Permite eliminar un usuario por su email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Error en la petición"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
	@DeleteMapping("/delete/{email}")
	public ResponseEntity<?> eliminar(@PathVariable String email) {
		
		switch(usuarioService.deleteUno(email)) {
			case 1:  return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
			case 0:  return new ResponseEntity<>("Usuario no existe", HttpStatus.NOT_FOUND);
			case -1: return new ResponseEntity<>("Esto es un problema de la base de datos, llame a servicio Tecnico", HttpStatus.BAD_REQUEST);
			default:  return new ResponseEntity<>("Error desconocido", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @Operation(summary = "Obtener perfil del usuario autenticado", description = "Devuelve la información del usuario autenticado según el token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil obtenido correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
	@GetMapping("/miperfil")
	public ResponseEntity<?> obtenerMiPerfil() {
		
	    // Obtener el email del token JWT
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String email = auth.getName();

	    // Buscar el usuario
	    Usuario usuario = usuarioService.buscarPorEmail(email);
	    if (usuario == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    // Crear respuesta DTO
	    UsuarioResponseDto response = UsuarioResponseDto.builder()
	            .email(usuario.getEmail())
	            .nombre(usuario.getNombre())
	            .apellidos(usuario.getApellidos())
	            .fechaRegistro(usuario.getFechaRegistro())
	            .rol(usuario.getRol().name()) // convertir enum a String
	            .enabled(usuario.getEnabled())
	            .build();

	    // Devolver respuesta
	    return ResponseEntity.ok(response);
	}

    @Operation(summary = "Editar perfil del usuario autenticado", description = "Permite actualizar los datos del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "403", description = "Intento de modificar otro usuario"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
	@PutMapping("/miperfil/edit")
	public ResponseEntity<?> editarMiPerfil(
	    @io.swagger.v3.oas.annotations.parameters.RequestBody(
	        description = "Datos a actualizar del usuario. El campo 'email' debe coincidir con el autenticado.",
	        required = true,
	        content = @Content(schema = @Schema(implementation = UsuarioRequestDto.class))
	    )
	    @RequestBody UsuarioRequestDto usuarioRequest
	) {

	    // Obtener email del usuario autenticado desde el token JWT
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String emailToken = auth.getName();

	    // Buscar el usuario autenticado
	    Usuario usuario = usuarioService.buscarPorEmail(emailToken);
	    if (usuario == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    // Validar que el email del request no intente suplantar identidad
	    if (usuarioRequest.getEmail() != null && !usuarioRequest.getEmail().equalsIgnoreCase(emailToken)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
	    }

	    // Actualizar los campos permitidos
	    usuario.setNombre(usuarioRequest.getNombre());
	    usuario.setApellidos(usuarioRequest.getApellidos());
	    // Si quiero permitir cambiar otras cosas, las pongo aquí ****
	    
	    
	    // Actualizar password si se ha enviado uno nuevo
	    if (usuarioRequest.getPassword() != null && !usuarioRequest.getPassword().isBlank()) {
	        String nuevaPasswordCodificada = passwordEncoder.encode(usuarioRequest.getPassword());
	        usuario.setPassword(nuevaPasswordCodificada);
	    }

	    // Guardar cambios bbdd
	    int result = usuarioService.updateUno(usuario);

	    if (result == 1) {
	        // Construir respuesta DTO
	        UsuarioResponseDto responseDto = UsuarioResponseDto.builder()
	                .email(usuario.getEmail())  // siempre email del token
	                .nombre(usuario.getNombre())
	                .apellidos(usuario.getApellidos())
	                .fechaRegistro(usuario.getFechaRegistro())
	                .rol(usuario.getRol().name())
	                .enabled(usuario.getEnabled())
	                .build();

	        return ResponseEntity.ok(responseDto);
	    }

	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}	

    @Operation(summary = "Reservar evento", description = "Permite al usuario autenticado reservar un evento por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva realizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Aforo completo"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "409", description = "Usuario ya inscrito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
	@PutMapping("/reservarevento/{idEvento}")
	public ResponseEntity<?> reservarEvento(@PathVariable int idEvento) {

	    // Obtener el email del usuario autenticado desde el token JWT
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String emailToken = auth.getName();

	    // Usar el servicio para intentar reservar
	    int resultado = reservaService.reservarEvento(emailToken, idEvento);

	    // Analizar la respuesta
	    switch (resultado) {
	        case 1: // OK
	            return ResponseEntity.ok().build(); 
	        case 2: // YA_INSCRITO
	            return ResponseEntity.status(HttpStatus.CONFLICT).build();
	        case 3: // AFORO_COMPLETO
	            return ResponseEntity.badRequest().build();
	        case 4: // NO_ENCONTRADO
	            return ResponseEntity.notFound().build();
	        default:
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
	    }
	}

    @Operation(summary = "Obtener reservas del usuario autenticado", description = "Devuelve las reservas que el usuario autenticado ha realizado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservas obtenidas correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MisReservasResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @GetMapping("/misreservas")
    public ResponseEntity<List<MisReservasResponseDto>> obtenerMisReservas() {
    	
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        List<MisReservasResponseDto> reservas = reservaService.misReservas(email);

        return ResponseEntity.ok(reservas);	    
	}	    

    @Operation(summary = "Inscribirse a un curso", description = "Permite al usuario autenticado inscribirse en un curso por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inscripción realizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Aforo completo"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "409", description = "Usuario ya inscrito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/inscribirsecurso/{idCurso}")
    public ResponseEntity<?> inscribirseCurso(@PathVariable int idCurso) {
    	
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        int resultado = inscripcionService.inscribirseCurso(email, idCurso);

        switch (resultado) {
	        case 1: // OK
	            return ResponseEntity.ok().build(); 
	        case 2: // YA_INSCRITO
	            return ResponseEntity.status(HttpStatus.CONFLICT).build();
	        case 3: // AFORO_COMPLETO
	            return ResponseEntity.badRequest().build();
	        case 4: // NO_ENCONTRADO
	            return ResponseEntity.notFound().build();
	        default:
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }
    

    @Operation(summary = "Obtener inscripciones del usuario autenticado", description = "Devuelve las inscripciones que el usuario autenticado ha realizado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inscripciones obtenidas correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MisInscripcionesResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @GetMapping("/misinscripciones")
    public ResponseEntity<List<MisInscripcionesResponseDto>> obtenerMisInscripciones() {
    	
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        List<MisInscripcionesResponseDto> inscripciones = inscripcionService.misInscripciones(email);

        return ResponseEntity.ok(inscripciones);
    }
 
    @Operation(summary = "Obtener inscripciones del usuario para calendario", description = "Devuelve las inscripciones que el usuario autenticado ha realizado en formato calendario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inscripciones para calendario obtenidas correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MisInscripcionesCalendarDto.class))),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @GetMapping("/misinscripcionescalendar")
    public ResponseEntity<List<MisInscripcionesCalendarDto>> obtenerMisInscripcionesParaCalendario() {
    	
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        List<MisInscripcionesCalendarDto> inscripcionesCalendar = inscripcionService.misInscripcionesParaCalendario(email);

        return ResponseEntity.ok(inscripcionesCalendar);
    } 

    @Operation(summary = "Obtener estadísticas del usuario autenticado", description = "Devuelve estadísticas de reservas e inscripciones del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadisticasUsuarioDto.class))),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @GetMapping("/misestadisticas")
    public ResponseEntity<EstadisticasUsuarioDto> obtenerMisEstadisticas() {
    	
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        EstadisticasUsuarioDto estadisticas = estadisticasService.obtenerEstadisticasUsuario(email);
        return ResponseEntity.ok(estadisticas);
    }
    
}
