import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CursoResponseDto } from '../../../interfaces/curso-response-dto';
import { PlanificacionCursoResponseDto } from '../../../interfaces/planificacion-curso-response-dto';
import { CursoService } from '../../../services/curso.service';
import { PlanificacionCursoService } from '../../../services/planificacion-curso.service';
import { InscripcionService } from '../../../services/inscripcion.service';
import { InscripcionResponseDto } from '../../../interfaces/inscripcion-response-dto';

@Component({
  selector: 'app-admin-cursos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-cursos.component.html',
  styleUrl: './admin-cursos.component.css'
})

export class AdminCursosComponent {
  cursos: CursoResponseDto[] = [];
  planificacionesPorCurso: Record<number, number> = {};
  inscripcionesPorCurso: Record<number, number> = {};

  private cursoService = inject(CursoService);
  private planificacionService = inject(PlanificacionCursoService);
  private inscripcionService = inject(InscripcionService);
  private router = inject(Router);

  ngOnInit() {
    // Carga inicial: obtiene todos los cursos y luego carga datos derivados
    this.cursoService.getTodas().subscribe({
      next: (data) => {
        this.cursos = data;
        this.cargarPlanificaciones(); // Cuenta planificaciones por curso
        this.cargarInscripciones();   // Cuenta inscripciones por curso
      },
      error: (err) => console.error('Error cargando cursos', err),
    });
  }

  cargarPlanificaciones() {
    // Para cada curso, se llama a la API individualmente y se guarda el número de planificaciones
    this.cursos.forEach(curso => {
      this.planificacionService.getPorCurso(curso.idCurso).subscribe({
        next: (planificaciones: PlanificacionCursoResponseDto[]) => {
          this.planificacionesPorCurso[curso.idCurso] = planificaciones.length;
        },
        error: () => {
          this.planificacionesPorCurso[curso.idCurso] = 0; // Evita fallos si algo sale mal
        }
      });
    });
  }

  cargarInscripciones() {
    // Igual que planificaciones, pero contando inscripciones por curso
    this.cursos.forEach(curso => {
      this.inscripcionService.getInscripcionesByCurso(curso.idCurso).subscribe({
        next: (inscripciones: InscripcionResponseDto[]) => {
          this.inscripcionesPorCurso[curso.idCurso] = inscripciones.length;
        },
        error: () => {
          this.inscripcionesPorCurso[curso.idCurso] = 0;
        }
      });
    });
  }

  esMinimoAsistenciaAlcanzado(curso: CursoResponseDto): boolean {
    // Comprueba si un curso ya alcanzó el mínimo de inscritos
    const inscripciones = this.inscripcionesPorCurso[curso.idCurso] || 0;
    return inscripciones >= curso.minimoAsistencia;
  }

  cancelar(id: number) {
    // Lógica de seguridad antes de permitir la cancelación
    const curso = this.cursos.find(c => c.idCurso === id);
    if (!curso) {
      alert('Curso no encontrado');
      return;
    }

    if (curso.estado === 'CANCELADO') {
      alert('Este curso ya está cancelado.');
      return;
    }

    // Confirmación antes de llamar al backend
    if (confirm('¿Estás seguro de que deseas cancelar este curso?')) {
      this.cursoService.cancelar(id).subscribe({
        next: () => {
          alert('Curso cancelado correctamente');
          this.ngOnInit(); // Recarga listado actualizado
        },
        error: () => alert('No se pudo cancelar el curso.')
      });
    }
  }

  aceptar(id: number) {
    const curso = this.cursos.find(c => c.idCurso === id);
    if (!curso) {
      alert('Curso no encontrado');
      return;
    }

    if (curso.estado === 'ACEPTADO') {
      alert('Este curso ya está aceptado.');
      return;
    }

    if (confirm('¿Estás seguro de que deseas aceptar este curso?')) {
      this.cursoService.aceptarCurso(id).subscribe({
        next: () => {
          alert('Curso aceptado correctamente');
          this.ngOnInit();
        },
        error: () => alert('No se pudo aceptar el curso.')
      });
    }
  }

  terminar(id: number) {
    const curso = this.cursos.find(c => c.idCurso === id);
    if (!curso) {
      alert('Curso no encontrado');
      return;
    }

    // Validaciones de estado para evitar acciones ilógicas
    if (curso.estado === 'TERMINADO') {
      alert('Este curso ya está terminado.');
      return;
    }

    if (curso.estado === 'CANCELADO') {
      alert('No se puede terminar un curso cancelado.');
      return;
    }

    if (confirm('¿Marcar como terminado este curso?')) {
      this.cursoService.terminarCurso(id).subscribe({
        next: () => {
          alert('Curso terminado correctamente');
          this.ngOnInit();
        },
        error: () => alert('No se pudo terminar el curso.')
      });
    }
  }

  volverAlPanel() {
    this.router.navigate(['/admin']);
  }
}

