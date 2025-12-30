package web.controller;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import web.auth.JwtUtils;
import web.dto.RegistroResponseDto;
import web.dto.UsuarioRequestDto;
import web.dto.UsuarioResponseDto;
import web.modelo.entities.Rol;
import web.modelo.entities.Usuario;
import web.modelo.services.AuthServiceImpl;


@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@Tag(name = "Admin", description = "Gestiones relacionadas con el rol de Administrador")
public class AdminController {
		
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
    private JwtUtils jwtUtils;
	
	@Autowired
	private AuthServiceImpl authService;

	@Operation(
		    summary = "Dar de alta un administrador",
		    description = "Crea un nuevo usuario con rol ADMINISTRADOR"
		)
		@ApiResponses(value = {
		    @ApiResponse(responseCode = "201", description = "Administrador creado correctamente",
		        content = @Content(schema = @Schema(implementation = RegistroResponseDto.class))),
		    @ApiResponse(responseCode = "409", description = "Email ya está en uso"),
		    @ApiResponse(responseCode = "400", description = "Error al crear el administrador")
		})
	@Transactional 
	@PostMapping("/alta/admin")
	public ResponseEntity<?> altaAdmin(@RequestBody UsuarioRequestDto altaAdminDto) {
		
		if (authService.buscarUno(altaAdminDto.getEmail()) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Ese email ya esta en uso, usa otro");
		}
		
	    Usuario usuario = new Usuario();
	    usuario.setEmail(altaAdminDto.getEmail());
	    usuario.setNombre(altaAdminDto.getNombre());
	    usuario.setApellidos(altaAdminDto.getApellidos());
	    usuario.setEnabled(1);
	    usuario.setFechaRegistro(new java.sql.Date(System.currentTimeMillis()));
	    usuario.setRol(Rol.ADMON); 
		
		// Se comenta por funcionalidad durante las pruebas
		// String password = PasswordGenerator.generateRandomPassword();
	    usuario.setPassword(passwordEncoder.encode(altaAdminDto.getPassword()));

	    authService.insertUno(usuario);

	    // Creo respuesta DTO
	    UsuarioResponseDto usuarioResponse = mapper.map(usuario, UsuarioResponseDto.class);
	    String token = jwtUtils.generateToken(usuario);
	    RegistroResponseDto response = new RegistroResponseDto(usuarioResponse, token);

	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(Map.of(
	                "message", "Admin cread@ con éxito",
	                "data", response
	            ));
	}

	@Operation(
		    summary = "Dar de alta un monitor",
		    description = "Crea un nuevo usuario con rol MONITOR"
		)
		@ApiResponses(value = {
		    @ApiResponse(responseCode = "201", description = "Monitor creado correctamente",
		        content = @Content(schema = @Schema(implementation = RegistroResponseDto.class))),
		    @ApiResponse(responseCode = "409", description = "Email ya está en uso"),
		    @ApiResponse(responseCode = "400", description = "Error al crear el monitor")
		})
	@Transactional 
	@PostMapping("/alta/monitor")
	public ResponseEntity<?> altaMonitor(@RequestBody UsuarioRequestDto altaMonitorDto) {
		
		if (authService.buscarUno(altaMonitorDto.getEmail()) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Ese email ya esta en uso, usa otro");
		}
		
	    Usuario usuario = new Usuario();
	    usuario.setEmail(altaMonitorDto.getEmail());
	    usuario.setNombre(altaMonitorDto.getNombre());
	    usuario.setApellidos(altaMonitorDto.getApellidos());
	    usuario.setEnabled(1);
	    usuario.setFechaRegistro(new java.sql.Date(System.currentTimeMillis()));
	    usuario.setRol(Rol.MONITOR); 
		
		// Se comenta por funcionalidad durante las pruebas
		// String password = PasswordGenerator.generateRandomPassword();
	    usuario.setPassword(passwordEncoder.encode(altaMonitorDto.getPassword()));

	    authService.insertUno(usuario);

	    // Creo respuesta DTO
	    UsuarioResponseDto usuarioResponse = mapper.map(usuario, UsuarioResponseDto.class);
	    String token = jwtUtils.generateToken(usuario);
	    RegistroResponseDto response = new RegistroResponseDto(usuarioResponse, token);

	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(Map.of(
	                "message", "Monitor cread@ con éxito",
	                "data", response
	            ));
		
	}

	@Operation(
		    summary = "Deshabilitar un usuario",
		    description = "Desactiva un usuario existente (enabled = 0)"
		)
		@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Usuario deshabilitado correctamente"),
		    @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
		    @ApiResponse(responseCode = "400", description = "Problema en la base de datos"),
		    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
		})
	@PutMapping("/deshabilitar/{email}")
	public ResponseEntity<Map<String, String>> baja(@PathVariable String email) {
	    Usuario usuario = authService.buscarUno(email);

	    if (usuario != null) {
	        usuario.setEnabled(0);
	        switch (authService.updateUno(usuario)) {
	            case 1:
	                return ResponseEntity.ok(Map.of("message", "Usuario deshabilitado correctamente"));
	            case 0:
	                return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                        .body(Map.of("message", "Usuario no existe"));
	            case -1:
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                        .body(Map.of("message", "Problema en la base de datos"));
	            default:
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                        .body(Map.of("message", "Error desconocido"));
	        }
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("message", "Este usuario no existe"));
	    }
	}

	@Operation(
		    summary = "Habilitar un usuario",
		    description = "Vuelve a habilitar un usuario previamente deshabilitado (enabled = 1)"
		)
		@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Usuario habilitado correctamente"),
		    @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
		    @ApiResponse(responseCode = "400", description = "Problema en la base de datos"),
		    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
		})
	@PutMapping("/habilitar/{email}")
	public ResponseEntity<Map<String, String>> habilitar(@PathVariable String email) {
	    Usuario usuario = authService.buscarUno(email);

	    if (usuario != null) {
	        usuario.setEnabled(1);
	        switch (authService.updateUno(usuario)) {
	            case 1:
	                return ResponseEntity.ok(Map.of("message", "Usuario habilitado correctamente"));
	            case 0:
	                return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                        .body(Map.of("message", "Usuario no encontrado"));
	            case -1:
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                        .body(Map.of("message", "Error en la base de datos"));
	            default:
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                        .body(Map.of("message", "Error desconocido"));
	        }
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("message", "Este usuario no existe"));
	    }
	}
	
	
}
