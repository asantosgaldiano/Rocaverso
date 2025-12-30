package web.modelo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="rocodromo")
public class Rocodromo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rocodromo")
    private int idRocodromo;

    private String nombre;
    
    @Column(name = "aforo_maximo")
    private int aforoMaximo;
    
    @Column(name = "metros_cuadrados")
    private int metrosCuadrados;
}
