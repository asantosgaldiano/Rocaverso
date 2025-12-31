import { Component, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CursoResponseDto } from '../../../interfaces/curso-response-dto';
import { CursoService } from '../../../services/curso.service';
import { UsuarioService } from '../../../services/usuario.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-monitor-dashboard',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './monitor-dashboard.component.html',
  styleUrl: './monitor-dashboard.component.css'
})

export class MonitorDashboardComponent {
  cursos = signal<CursoResponseDto[]>([]);
  error = signal<string | null>(null);
  nombreMonitor = signal<string>('Monitor');

  constructor(
    private cursoService: CursoService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {
    this.cargarDatos();
  }

  cargarDatos() {
    this.usuarioService.obtenerMiPerfil().subscribe({
      next: (data) => {
        this.nombreMonitor.set(data.nombre);
      },
      error: () => {
        this.nombreMonitor.set('Monitor');
      }
    });

    this.cursoService.getCursosDelMonitor().subscribe({
      next: (data) => {
        this.cursos.set(data);
      },
      error: () => {
        this.error.set('No se pudieron cargar los cursos del monitor.');
      }
    });
  }

  // verDetalle(id: number) {
  //   this.router.navigate(['/monitor/curso', id]);
  // }

  verDetalle(idCurso: number) {
    this.router.navigate(['/curso', idCurso]);
  }

  irACalendario() {
    this.router.navigate(['/monitor/calendario']);
  }
}
