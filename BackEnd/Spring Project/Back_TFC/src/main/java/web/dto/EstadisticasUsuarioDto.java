package web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.modelo.entities.Dificultad;
import web.modelo.entities.TipoVia;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticasUsuarioDto {

    private int totalViasRealizadas;
    private Dificultad nivelMedio;
    private TipoVia tipoMasFrecuente; 
}
