import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { CursoService } from '../../../../services/curso.service';
import { UsuarioService } from '../../../../services/usuario.service';
import { PlanificacionCursoService } from '../../../../services/planificacion-curso.service';
import { CursoRequestDto } from '../../../../interfaces/curso-request-dto';

@Component({
  selector: 'app-curso-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './curso-edit.component.html',
  styleUrl: './curso-edit.component.css'
})

export class CursoEditComponent implements OnInit {
  form!: FormGroup;
  idCurso!: number;
  error: string | null = null;
  monitores: any[] = [];
  planificacionesGuardadas: boolean[] = [];  // Marca qué planificaciones están sincronizadas con BD

  zonas: string[] = [
    'DEPORTIVA', 'BOULDER', 'ENTRENAMIENTO', 'INFANTIL',
    'VESTUARIOS', 'AULAS', 'CAFETERIA', 'EXTERIOR'
  ];

  diasSemana: string[] = [
    'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'
  ];

  // Signal para renderizar dinámicamente con @for (mejor que usar *ngFor)
  planificaciones = signal<any[]>([]);

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cursoService = inject(CursoService);
  private usuarioService = inject(UsuarioService);
  private planificacionService = inject(PlanificacionCursoService);

  ngOnInit(): void {
    // Obtiene id del curso desde la ruta
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('idCurso');
      if (!idParam) {
        this.error = 'ID de curso no válido';
        return;
      }

      this.idCurso = +idParam;

      // Formulario del curso (igual que en Alta), con validadores globales
      this.form = this.fb.group({
        nombre: ['', Validators.required],
        descripcion: ['', Validators.required],
        fechaInicio: ['', Validators.required],
        fechaFin: ['', Validators.required],
        aforoMaximo: [0, [Validators.required, Validators.min(1)]],
        minimoAsistencia: [0, [Validators.required, Validators.min(0)]],
        precio: [0, [Validators.required, Validators.min(0)]],
        zona: ['', Validators.required],
        idRocodromo: [1, Validators.required],
        emailMonitor: ['', Validators.required],

        // Las planificaciones se cargarán dinámicamente
        planificaciones: this.fb.array([])

      }, { validators: [this.validarFechas, this.validarAforo] });

      // Carga monitores y luego carga el curso
      this.usuarioService.getTodos().subscribe({
        next: data => {
          this.monitores = data.filter(u => u.rol === 'MONITOR');
          this.cargarCurso();                      // Carga datos principales del curso
        },
        error: err => {
          console.error('Error cargando monitores:', err);
          this.error = 'No se pudieron cargar los monitores.';
        }
      });
    });
  }

  // Acceso directo al FormArray de las planificaciones
  get planificacionesForm(): FormArray {
    return this.form.get('planificaciones') as FormArray;
  }

  private cargarCurso(): void {
    // Carga datos base del curso
    this.cursoService.getDetalle(this.idCurso).subscribe({
      next: curso => {
        // Busca el monitor en la lista ya cargada
        const monitor = this.monitores.find(m => m.nombre === curso.nombreMonitor);
        const monitorEmail = monitor?.email ?? '';

        // Patch para rellenar los campos del formulario
        this.form.patchValue({
          nombre: curso.nombre,
          descripcion: curso.descripcion,
          fechaInicio: curso.fechaInicio.split('T')[0],  // Formato yyyy-MM-dd para el input
          fechaFin: curso.fechaFin.split('T')[0],
          aforoMaximo: curso.aforoMaximo,
          minimoAsistencia: curso.minimoAsistencia,
          precio: curso.precio,
          zona: curso.zona,
          idRocodromo: 1,
          emailMonitor: monitorEmail
        });

        // Carga las planificaciones asociadas
        this.cargarPlanificaciones();
      },
      error: err => {
        console.error('Error cargando curso:', err);
        this.error = 'No se pudo cargar el curso.';
      }
    });
  }

  private cargarPlanificaciones(): void {
    // Trae las planificaciones almacenadas en BD y las inserta en el FormArray
    this.planificacionService.getPorCurso(this.idCurso).subscribe({
      next: datos => {
        this.planificaciones.set(datos ?? []);       // Para render @for
        this.planificacionesForm.clear();            // Limpia FormArray
        this.planificacionesGuardadas = [];          // Resetea estado guardado

        datos.forEach((p, index) => {
          // Se crea un FormGroup para cada planificación
          const fg = this.fb.group({
            diaSemana: [p.diaSemana, Validators.required],
            horaInicio: [p.horaInicio, Validators.required],
            horaFin: [p.horaFin, Validators.required]
          }, 
          { validators: 
            [this.validarHorasPlanificacion] 
          });

          // Si el usuario modifica algo - marcar como "no guardado"
          fg.valueChanges.subscribe(() => {
            this.planificacionesGuardadas[index] = false;
          });

          this.planificacionesForm.push(fg);
          this.planificacionesGuardadas.push(true); // Inicialmente está guardada
        });
      },
      error: err => {
        console.error('Error cargando planificaciones:', err);
        this.planificaciones.set([]);
        this.planificacionesGuardadas = [];
      }
    });
  }

  addPlanificacion(): void {
    // Añade planificación nueva al formulario
    const nuevoFormGroup = this.fb.group({
      diaSemana: ['', Validators.required],
      horaInicio: ['', Validators.required],
      horaFin: ['', Validators.required]
    }, 
    { validators: 
      [this.validarHorasPlanificacion] 
    });

    const index = this.planificacionesForm.length;

    // Marca planificación como "no guardada" si se edita
    nuevoFormGroup.valueChanges.subscribe(() => {
      this.planificacionesGuardadas[index] = false;
    });

    this.planificacionesForm.push(nuevoFormGroup);
    this.planificaciones.set(this.planificacionesForm.value);  // Actualiza signal
    this.planificacionesGuardadas.push(false);
  }

  eliminarPlanificacion(index: number): void {
    // Elimina del FormArray y del signal
    this.planificacionesForm.removeAt(index);
    this.planificaciones.set(this.planificacionesForm.value);

    // También elimina su estado guardado
    this.planificacionesGuardadas.splice(index, 1);

    console.log(`Eliminada planificación ${index}`);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.error = 'Formulario inválido. Revisa los campos.';
      return;
    }

    // El DTO se extrae directamente del form
    const dto: CursoRequestDto = {
      ...this.form.value
    };
    const emailMonitor = dto.emailMonitor;    
    
    // Pido los cursos del monitor al backend
    this.cursoService.getCursosMonitor(emailMonitor).subscribe({

      next: cursosMonitor => {

        // Excluir el curso actual (en edición no debe compararse consigo mismo)
        const otros = cursosMonitor.filter(c => c.idCurso !== this.idCurso);

        // No hay solapes - actualizar curso
        if (this.existenSolapes(dto.planificaciones, otros)) {
          this.error = "El monitor ya tiene un curso que solapa en horario.";
          return;
        }

        // Si TODO OK → guardar
        this.cursoService.editar(this.idCurso, dto).subscribe({
          next: () => {
            alert('Curso actualizado correctamente');
            this.router.navigate(['/admin/cursos']);
          },
          error: () => this.error = 'No se pudo actualizar el curso.'
        });
      },

      // Error al obtener datos del monitor
      error: () => {
        this.error = "No se pudo validar disponibilidad del monitor.";
      }
    });


    // Llamada al backend para actualizar el curso
    // this.cursoService.editar(this.idCurso, dto).subscribe({
    //   next: () => {
    //     alert('Curso actualizado correctamente');
    //     this.router.navigate(['/admin/cursos']);
    //   },
    //   error: err => {
    //     this.error = 'No se pudo actualizar el curso.';
    //     console.error('Error al actualizar curso:', err);
    //   }
    // });
  }

  // Comprueba si las nuevas planificaciones entran en conflicto con cursos previos del monitor
  existenSolapes(planNuevas: any[], cursosMonitor: any[]): boolean {
    for (const curso of cursosMonitor) {
      for (const p of curso.planificaciones) {
        for (const nueva of planNuevas) {

          // Mismo día - posible conflicto
          if (p.diaSemana === nueva.diaSemana) {
            // Solape real si NO se cumple:
            // nuevaFin <= inicioViejo  ó  nuevaInicio >= finViejo
            // (es decir, los intervalos se pisan)
            if (!(nueva.horaFin <= p.horaInicio || nueva.horaInicio >= p.horaFin)) {
              return true; // Existe conflicto de horario
            }
          }
        }
      }
    }
    return false; // No hay solapes
  }

  checkControl(controlName: string, errorName: string): boolean {
    const control = this.form.get(controlName);
    return !!(control && control.hasError(errorName) && control.touched);
  }
  
  // Validación de rango de fechas
  validarFechas(group: FormGroup) {
    const inicio = new Date(group.get('fechaInicio')?.value);
    const fin = new Date(group.get('fechaFin')?.value);
    if (fin < inicio) {
      return { fechaInvalida: true };
    }
    return null;
  }

  // Validación de lógica entre aforo y mínimo
  validarAforo(group: FormGroup) {
    const aforo = group.get('aforoMaximo')?.value;
    const minimo = group.get('minimoAsistencia')?.value;
    if (aforo < minimo) {
      return { aforoInvalido: true };
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

}

