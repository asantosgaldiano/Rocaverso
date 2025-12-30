package web.modelo.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name="cursos")
public class Curso implements Serializable{
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private int idCurso;

    private String nombre;
    private String descripcion;

    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Column(name = "fecha_fin")
    private Date fechaFin;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "aforo_maximo")
    private int aforoMaximo;

    @Column(name = "minimo_asistencia")
    private int minimoAsistencia;

    private BigDecimal precio;
    
    @Enumerated(EnumType.STRING)
    private Zona zona;

    // Un curso puede tener varias planificaciones (días/horarios)
    @OneToMany(mappedBy = "curso", // El campo curso en PlanificacionCurso es el dueño de la relación.
    		cascade = CascadeType.ALL, // Si se guarda, actualiza o borra un curso, se hace lo mismo con sus planificaciones.
    		orphanRemoval = true, // Si se elimina una planificación de la lista, también se borra de la base de datos.
    		fetch = FetchType.LAZY)
    private List<PlanificacionCurso> planificaciones;
    
    @ManyToOne(fetch = FetchType.LAZY) // Se cargan solo cuando se necesitan, no automáticamente.
    @JoinColumn(name = "id_rocodromo")
    private Rocodromo rocodromo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_monitor", nullable = false) // Siempre debe haber un monitor asociado.
    private Usuario monitor;
}
