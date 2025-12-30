package web.modelo.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="eventos")
public class Evento implements Serializable {
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_evento")
	private int idEvento;
	
	private String nombre;
	private String descripcion;
	
	@Column(name="fecha_inicio")
	private Date fechaInicio;	

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

	// Esto llama al ENUM "Estado"
	@Enumerated(EnumType.STRING) // Para el tipo de la BBDDD
	private Estado estado;
	
	// Esto llama al ENUM "Destacado"
	@Enumerated(EnumType.STRING) // Para el tipo de la BBDDD
	private Destacado destacado;

	@Column(name="aforo_maximo")
	private int aforoMaximo;
	
	@Column(name="minimo_asistencia")
	private int minimoAsistencia;
	
	private BigDecimal precio;
	private String imagen;
	
    @Enumerated(EnumType.STRING)
    private Zona zona;
	
	//uni-directional many-to-one 
	@ManyToOne // Son muchos eventos de un tipo
	@JoinColumn(name="id_tipo")
	private Tipo tipo;
	
    @ManyToOne
    @JoinColumn(name="id_rocodromo")
    private Rocodromo rocodromo;
}
