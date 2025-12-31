import { Component, inject, OnInit, ViewEncapsulation } from '@angular/core';
import { EstadisticasCardComponent } from "../../../components/usuario/estadisticas-card/estadisticas-card.component";
import { ViasRealizadasCardComponent } from "../../../components/usuario/vias-realizadas-card/vias-realizadas-card.component";
import { MisInscripcionesCardComponent } from "../../../components/usuario/mis-inscripciones-card/mis-inscripciones-card.component";
import { MisReservasCardComponent } from "../../../components/usuario/mis-reservas-card/mis-reservas-card.component";
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-usuario-dashboard',
  standalone: true,
  imports: [EstadisticasCardComponent, ViasRealizadasCardComponent, MisInscripcionesCardComponent, MisReservasCardComponent],
  templateUrl: './usuario-dashboard.component.html',
  styleUrl: './usuario-dashboard.component.css',
  encapsulation: ViewEncapsulation.None
})

export class UsuarioDashboardComponent implements OnInit {

  nombreUsuario: string = '';  
  private usuarioService = inject(UsuarioService);

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.usuarioService.obtenerMiPerfil().subscribe({
      next: (data) => {
        this.nombreUsuario = data.nombre;
      },
      error: (error) => {
        console.error('Error al obtener perfil del usuario:', error);
      }
    });
  }

  irACalendario() {
    this.router.navigate(['/usuario/calendario']);
  }
}
