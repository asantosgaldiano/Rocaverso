package web.modelo.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="usuarios")
public class Usuario implements Serializable, UserDetails{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String email;
	
	private String nombre;
	private String apellidos;
	private String password;
	
	@Builder.Default
	private int enabled = 1;
	
	@Column(name="fecha_Registro")
	private Date fechaRegistro;
	
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private Rol rol = Rol.USUARIO;
	

	public Rol getTipoRol() {
	    return this.rol != null ? this.rol : null;
	}

		
	@Override // Spring Security y devuelve los roles o permisos del usuario autenticado.
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    Rol rol = getTipoRol();
	    if (rol == null) return Collections.emptyList();
	    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol));
	}


    @Override
    public String getUsername() {
        return this.email;
    }
    
    @Override
    public String getPassword() {
        return this.password;
    }
}
