import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CursoResponseDto } from '../../../interfaces/curso-response-dto';
import { AuthService } from '../../../services/auth.service';
import { InscripcionService } from '../../../services/inscripcion.service';
import { UsuarioService } from '../../../services/usuario.service';
import { CursoService } from '../../../services/curso.service';


@Component({
  selector: 'app-curso-botonera',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './curso-botonera.component.html',
  styleUrl: './curso-botonera.component.css'
})

export class CursoBotoneraComponent {

  // Curso recibido desde el componente padre
  @Input() curso!: CursoResponseDto;

  // Inyección de servicios necesarios
  auth = inject(AuthService);
  router = inject(Router);
  usuarioService = inject(UsuarioService);
  inscripcionService = inject(InscripcionService);
  cursoService = inject(CursoService);

  editar() {
    // Redirige al formulario de edición del curso
    this.router.navigate(['/admin/cursos/editar', this.curso.idCurso]);
  }

  eliminar() {
    // Confirmación previa antes de borrar un curso
    const confirmacion = confirm(`¿Estás seguro de que quieres eliminar el curso "${this.curso.nombre}"?`);
    if (!confirmacion) return;

    // Llamada al backend para eliminar el curso
    this.cursoService.eliminar(this.curso.idCurso).subscribe({
      next: () => {
        alert('Curso eliminado correctamente');
        // Regresa al listado
        this.router.navigate(['/admin/cursos']);
      },
      error: (err) => {
        // Manejo de errores común
        if (err.status === 404) {
          alert('Curso no encontrado');
        } else if (err.status === 403) {
          alert('No tienes permisos para eliminar este curso');
        } else {
          alert('Error inesperado al eliminar el curso');
        }
      }
    });
  }

  inscribir() {
    // El usuario intenta inscribirse en el curso
    this.usuarioService.inscribirseCurso(this.curso.idCurso).subscribe({
      next: () => {
        alert('Inscripción realizada correctamente');
      },
      error: (err) => {
        // Manejo de errores enviados por el backend
        if (err.status === 409) {
          alert('Ya estás inscrito en este curso');
        } else if (err.status === 400) {
          alert('Aforo completo para este curso');
        } else if (err.status === 404) {
          alert('Curso no encontrado');
        } else {
          alert('Error inesperado al inscribirse en el curso');
        }
      }
    });
  }

  cancelarInscripcion() {
    // El usuario cancela su inscripción
    this.inscripcionService.cancelarInscripcion(this.curso.idCurso).subscribe({
      next: () => {
        alert('Inscripción cancelada correctamente');
      },
      error: (err) => {
        if (err.status === 404) {
          alert('No se encontró la inscripción');
        } else if (err.status === 500) {
          alert('Error del servidor al cancelar la inscripción');
        } else {
          alert('Error inesperado al cancelar la inscripción');
        }
      }
    });
  }

  volverAlPanel() {
    this.router.navigate(['/usuario']);
  }
}
