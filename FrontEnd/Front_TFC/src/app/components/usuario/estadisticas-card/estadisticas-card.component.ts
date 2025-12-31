import { Component, OnInit } from '@angular/core';
import { EstadisticasUsuarioDto } from '../../../interfaces/estadisticas-usuario-dto';
import { UsuarioService } from '../../../services/usuario.service';

@Component({
  selector: 'app-estadisticas-card',
  standalone: true,
  imports: [],
  templateUrl: './estadisticas-card.component.html',
  styleUrl: './estadisticas-card.component.css'
})

export class EstadisticasCardComponent implements OnInit {

  // Guarda las estadísticas devueltas por el backend
  estadisticas: EstadisticasUsuarioDto | null = null;

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    // Petición al backend para cargar las estadísticas del usuario
    this.usuarioService.obtenerMisEstadisticas().subscribe({
      next: (data) => {
        // Asigno los datos recibidos al componente
        this.estadisticas = data;
      },
      error: (err) => {
        // Registro del error por si ocurre un fallo en la petición
        console.error('Error cargando estadísticas', err);
      }
    });
  }
}
