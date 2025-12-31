import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CursoResponseDto } from '../../../interfaces/curso-response-dto';
import { InscripcionResponseDto } from '../../../interfaces/inscripcion-response-dto';
import { CursoService } from '../../../services/curso.service';
import { InscripcionService } from '../../../services/inscripcion.service';
import { CommonModule } from '@angular/common';
import { PlanificacionCursoResponseDto } from '../../../interfaces/planificacion-curso-response-dto';
import { PlanificacionCursoService } from '../../../services/planificacion-curso.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-monitor-curso-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './monitor-curso-detail.component.html',
  styleUrl: './monitor-curso-detail.component.css'
})

export class MonitorCursoDetailComponent {

  private route = inject(ActivatedRoute);
  private cursoService = inject(CursoService);
  private inscripcionService = inject(InscripcionService);
  private planificacionService = inject(PlanificacionCursoService);

  curso!: CursoResponseDto;
  inscripciones = signal<InscripcionResponseDto[]>([]);
  planificaciones = signal<PlanificacionCursoResponseDto[]>([]);
  cargando = true;

  ngOnInit() {
    const idCurso = Number(this.route.snapshot.paramMap.get('idCurso'));
    if (isNaN(idCurso)) {
      console.error('ID de curso no válido');
      this.cargando = false;
      return;
    }

    // Cargar detalles del curso
    this.cursoService.getDetalle(idCurso).subscribe({
      next: (data) => {
        this.curso = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando curso:', err);
        this.cargando = false;
      }
    });

    // Cargar lista de inscripciones
    this.inscripcionService.getInscripcionesByCurso(idCurso).subscribe({
      next: (lista) => this.inscripciones.set(lista ?? []),
      error: (err) => {
        console.error('Error cargando inscripciones:', err);
        this.inscripciones.set([]);
      }
    });

    // Cargar lista de planificacion del curso
    this.planificacionService.getPorCurso(idCurso).subscribe({
      next: (datos) => {
        this.planificaciones.set(datos ?? []);
      },
      error: (err) => {
        console.error('Error cargando planificaciones:', err);
        this.planificaciones.set([]);
      }
    });
  }

  get aforo(): string {
    const ratio = this.curso.plazasLibres / this.curso.aforoMaximo;

    if (ratio === 0) {
      return 'bg-danger';
    } else if (ratio <= 0.25) {
      return 'bg-warning text-dark';
    } else {
      return 'bg-success';
    }
  }
}
