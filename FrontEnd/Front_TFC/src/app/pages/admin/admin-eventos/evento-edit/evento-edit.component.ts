import { CommonModule } from '@angular/common';
import { Component, inject, OnInit  } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { EventoRequestDto } from '../../../../interfaces/evento-request-dto';
import { EventoService } from '../../../../services/evento.service';
import { Tipo } from '../../../../interfaces/tipo';
import { TipoService } from '../../../../services/tipo.service';

@Component({
  selector: 'app-evento-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './evento-edit.component.html',
  styleUrl: './evento-edit.component.css'
})
export class EventoEditComponent implements OnInit{

  form!: FormGroup;
  idEvento!: number;
  loading = false;
  error: string | null = null;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private eventoService = inject(EventoService);
  private tipoService = inject(TipoService);
  tipos: Tipo[] = [];

  zonas: string[] = [
    'DEPORTIVA',
    'BOULDER',
    'ENTRENAMIENTO',
    'INFANTIL',
    'VESTUARIOS',
    'AULAS',
    'CAFETERIA',
    'EXTERIOR'
  ];

  ngOnInit(): void {

    this.route.paramMap.subscribe(params => {
      const idParam = params.get('idEvento');

      if (!idParam) {
        this.error = 'ID de evento no válido';
        return;
      }

    this.idEvento = +idParam;

    this.form = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: ['', Validators.required],
      fechaInicio: ['', Validators.required],
      horaInicio: ['', Validators.required],
      horaFin: ['', Validators.required],
      aforoMaximo: [0, [Validators.required, Validators.min(1)]],
      minimoAsistencia: [0, [Validators.required, Validators.min(0)]],
      precio: [0, [Validators.required, Validators.min(0)]],
      imagen: ['', Validators.required],
      idTipo: [null, Validators.required],
      zona: ['', Validators.required],
      idRocodromo: [1, Validators.required], // OJO SI CREO MAS SEDES DE ROCODROMOS
    },
      {
        validators: [this.validarMinimoAforo,
          this.validarHoras
        ]
      }
    );

    this.tipoService.getTodos().subscribe({
      next: tipos => {
        this.tipos = tipos;

        // Ahora que ya tenemos los tipos, pedimos el evento
        this.eventoService.getDetalle(this.idEvento).subscribe({
          next: evento => {
            const fecha = evento.fechaInicio.split('T')[0];

            // Buscar el tipo en la lista
            const tipoEncontrado = this.tipos.find(t => t.nombre === evento.tipo);
            const idTipo = tipoEncontrado ? tipoEncontrado.idTipo : null;

            this.form.patchValue({
              ...evento,
              fechaInicio: fecha,
              idTipo,
              idRocodromo: 1 // OJO SI CREO MAS SEDES DE ROCODROMOS
            });
          },
          error: err => {
            this.error = 'No se pudo cargar el evento.';
            console.error('[EventoEditComponent] Error al obtener evento:', err);
          }
        });

      },
      error: err => {
        console.error('[EventoEditComponent] Error al cargar tipos:', err);
        this.error = 'No se pudieron cargar los tipos de evento.';
      }
    });
    });
  }

  onSubmit() {
    if (this.form.invalid) {
      console.warn('[EventoEditComponent] Formulario inválido al enviar');
      return;
    }

    const dto: EventoRequestDto = this.form.value;

    this.eventoService.editarEvento(this.idEvento, dto).subscribe({
      next: () => {
        alert('Evento actualizado correctamente');
        this.router.navigate(['/admin/eventos']);
      },
      error: err => {
        this.error = 'No se pudo actualizar el evento.';
        console.error('[EventoEditComponent] Error al actualizar evento:', err);
      }
    });
  }

  checkControl(controlName: string, errorName: string): boolean {
    const control = this.form.get(controlName);
    return !!(control && control.hasError(errorName) && control.touched);
  }

  validarMinimoAforo(form: FormGroup) {
    const aforo = form.get('aforoMaximo')?.value;
    const minimo = form.get('minimoAsistencia')?.value;

    if (minimo != null && aforo != null && minimo > aforo) {
      return { minimoMayorQueAforo: true };
    }
    return null;
  }

  validarHoras(form: FormGroup) {
    const inicio = form.get('horaInicio')?.value;
    const fin = form.get('horaFin')?.value;

    if (!inicio || !fin) return null;

    // Comparar como "HH:mm"
    if (fin < inicio) {
      return { horaFinMenor: true };
    }

    return null;
  }
}
