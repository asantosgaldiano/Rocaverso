import { Component, inject, signal } from '@angular/core';
import { CursoService } from '../../../services/curso.service';
import { CursoResponseDto } from '../../../interfaces/curso-response-dto';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { PlanificacionCursoResponseDto } from '../../../interfaces/planificacion-curso-response-dto';
import { PlanificacionCursoService } from '../../../services/planificacion-curso.service';
import { CursoBotoneraComponent } from "../../../components/public/curso-botonera/curso-botonera.component";
import { UsuarioService } from '../../../services/usuario.service';
import { InscripcionResponseDto } from '../../../interfaces/inscripcion-response-dto';
import { InscripcionService } from '../../../services/inscripcion.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-curso-detail',
  standalone: true,
  imports: [RouterModule, CommonModule, CursoBotoneraComponent],
  templateUrl: './curso-detail.component.html',
  styleUrl: './curso-detail.component.css'
})

export class CursoDetailComponent {

  auth = inject(AuthService);
  private route = inject(ActivatedRoute);
  private cursoService = inject(CursoService);
  private planificacionService = inject(PlanificacionCursoService);
  private usuarioService = inject(UsuarioService);
  private inscripcionService = inject(InscripcionService);

  curso!: CursoResponseDto; 
  planificaciones = signal<PlanificacionCursoResponseDto[]>([]);
  inscripciones = signal<InscripcionResponseDto[]>([]);

  cargando = true;

  ngOnInit() {
    const idCurso = Number(this.route.snapshot.paramMap.get('idCurso'));
    if (isNaN(idCurso)) {
      console.error('ID no válido');
      this.cargando = false;
      return;
    }

    this.cursoService.getDetalle(idCurso).subscribe({
      next: (data) => {
        this.curso = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando curso:', err);
        this.cargando = false;
      }
    })

    this.planificacionService.getPorCurso(idCurso).subscribe({
      next: (datos) => {
        this.planificaciones.set(datos ?? []);
      },
      error: (err) => {
        console.error('Error cargando planificaciones:', err);
        this.planificaciones.set([]);
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
  }


  get aforo(): string {
    const ratio = this.curso.plazasLibres / this.curso.aforoMaximo;

    if (ratio === 0) {
      return 'bg-danger';       // Sin plazas libres: rojo
    } else if (ratio <= 0.25) {
      return 'bg-warning text-dark';  // Poco aforo: amarillo con texto oscuro
    } else {
      return 'bg-success';      // Suficiente aforo: verde
    }
  }
}
