import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CursoRequestDto } from '../../../../interfaces/curso-request-dto';
import { CursoService } from '../../../../services/curso.service';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../../../services/usuario.service';

@Component({
  selector: 'app-curso-add',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './curso-add.component.html',
  styleUrl: './curso-add.component.css'
})

export class CursoAddComponent implements OnInit {
  form!: FormGroup;

  // Opciones estáticas para selects de zona del rocódromo
  zonas: string[] = [
    'DEPORTIVA', 'BOULDER', 'ENTRENAMIENTO', 'INFANTIL',
    'VESTUARIOS', 'AULAS', 'CAFETERIA', 'EXTERIOR'
  ];

  // Opciones estáticas para los días donde se puede planificar el curso
  diasSemana: string[] = [
    'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'
  ];

  error: string | null = null;
  monitores: any[] = [];

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private cursoService = inject(CursoService);
  private usuarioService = inject(UsuarioService);

  ngOnInit(): void {
    // Formulario reactivo del curso completo
    // Incluye validadores a nivel de grupo (fechas y aforo)
    this.form = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: ['', Validators.required],
      fechaInicio: ['', Validators.required],
      fechaFin: ['', Validators.required],
      aforoMaximo: [0, [Validators.required, Validators.min(1)]],
      minimoAsistencia: [0, [Validators.required, Validators.min(0)]],
      precio: [0, [Validators.required, Validators.min(0)]],
      zona: ['', Validators.required],
      idRocodromo: [1], // Id fijo; se podría cargar dinámicamente
      emailMonitor: ['', Validators.required],

      // Array de planificaciones - cada entrada es un día con hora inicio/fin
      planificaciones: this.fb.array([])

    }, { validators: [this.validarFechas, this.validarAforo] });

    // Cargar lista de monitores (usuarios con rol MONITOR)
    this.usuarioService.getTodos().subscribe({
      next: data => {
        this.monitores = data.filter(u => u.rol === 'MONITOR');
        this.addPlanificacion(); // Crea una primera fila vacía
      },
      error: err => {
        console.error('Error cargando monitores:', err);
        this.error = 'No se pudieron cargar los monitores.';
      }
    });
  }

  // Getter para manejar cómodamente el FormArray
  get planificacionesForm(): FormArray {
    return this.form.get('planificaciones') as FormArray;
  }

  addPlanificacion(): void {
    // Añade una nueva planificación (día + horas) al FormArray
    this.planificacionesForm.push(this.fb.group({
      diaSemana: ['', Validators.required],
      horaInicio: ['', Validators.required],
      horaFin: ['', Validators.required],
      idCurso: [0] // El backend lo asigna luego
      }, 
      { validators: [this.validarHorasPlanificacion] })
    );
  }

  eliminarPlanificacion(i: number): void {
    // Elimina la planificación seleccionada
    this.planificacionesForm.removeAt(i);
  }

  onSubmit(): void {
    // Si el formulario no es válido - marcar errores y mostrar alertas
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      const errores = this.form.errors || {};
      if (errores['fechaInvalida']) {
        alert('La fecha de fin no puede ser anterior a la de inicio.');
      }
      if (errores['aforoInvalido']) {
        alert('El aforo máximo no puede ser menor que el mínimo de asistencia.');
      }
      return;
    }

    // El DTO final del curso sale directamente del FormGroup
    // const curso: CursoRequestDto = this.form.value;
    const dto: CursoRequestDto = this.form.value;
    const emailMonitor = dto.emailMonitor;

    // Cargar cursos actuales del monitor
    this.cursoService.getCursosMonitor(emailMonitor).subscribe({

      next: cursosMonitor => {

        // Comprobación de solapes entre nuevas planificaciones y cursos existentes
        if (this.existenSolapes(dto.planificaciones, cursosMonitor)) {
          this.error = "El monitor ya tiene un curso que solapa en horario.";
          return;
        }

        // Si no hay solapes - crear curso
        this.cursoService.crear(dto).subscribe({
          next: () => this.router.navigate(['/admin/cursos']),
          error: err => alert('Error al crear curso: ' + err.message)
        });
      },

      // Error al obtener cursos del monitor
      error: () => {
        this.error = "No se pudo validar disponibilidad del monitor.";
      }
    });

    // this.cursoService.crear(curso).subscribe({
    //   next: () => this.router.navigate(['/admin/cursos']),
    //   error: (err) => alert('Error al crear curso: ' + err.message)
    // });
  }

  // Recorre todas las planificaciones del monitor y compara con las nuevas
  existenSolapes(planNuevas: any[], cursosMonitor: any[]): boolean {
    for (const curso of cursosMonitor) {
      for (const p of curso.planificaciones) {
        for (const nueva of planNuevas) {
        
          // Si coinciden en el mismo día puede haber conflicto
          if (p.diaSemana === nueva.diaSemana) {

            // NO solapan si:
            // nueva.fin <= inicioViejo  O  nueva.inicio >= finViejo
            // Si no se cumple - los intervalos se pisan
            if (!(nueva.horaFin <= p.horaInicio || nueva.horaInicio >= p.horaFin)) {
              return true; // Hay solape real
            }
          }
        }
      }
    }
    return false; // No hay conflictos
  }

  checkControl(controlName: string, errorName: string): boolean {
    const control = this.form.get(controlName);
    return !!(control && control.hasError(errorName) && control.touched);
  }

  validarFechas(group: FormGroup) {
    // Obtiene valores del formulario y los convierte a Date
    const inicio = new Date(group.get('fechaInicio')?.value);
    const fin = new Date(group.get('fechaFin')?.value);

    // Regla: fin debe ser posterior o igual al inicio
    if (fin < inicio) {
      return { fechaInvalida: true };
    }
    return null;
  }

  validarHorasPlanificacion(group: FormGroup) {
    const inicio = group.get('horaInicio')?.value;
    const fin = group.get('horaFin')?.value;

    if (!inicio || !fin) return null;

    if (fin < inicio) {
      return { horaFinMenor: true };
    }

    return null;
  }

  validarAforo(group: FormGroup) {
    // Valida las magnitudes de aforo máximo y aforo mínimo
    const aforo = group.get('aforoMaximo')?.value;
    const minimo = group.get('minimoAsistencia')?.value;

    if (aforo < minimo) {
      return { aforoInvalido: true };
    }
    return null;
  }

}

