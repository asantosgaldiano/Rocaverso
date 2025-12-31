import { Component, inject, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { EventoRequestDto } from '../../../../interfaces/evento-request-dto';
import { Tipo } from '../../../../interfaces/tipo';
import { EventoService } from '../../../../services/evento.service';
import { TipoService } from '../../../../services/tipo.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-evento-add',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './evento-add.component.html',
  styleUrl: './evento-add.component.css'
})
export class EventoAddComponent implements OnInit {

  form!: FormGroup;
  error: string | null = null;

  private fb = inject(FormBuilder);
  private router = inject(Router);
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
      idRocodromo: [1, Validators.required] // Sede por defecto
    },
    {
      validators: [this.validarMinimoAforo, this.validarHoras]
    }
  );

    this.tipoService.getTodos().subscribe({
      next: tipos => this.tipos = tipos,
      error: err => {
        console.error('[EventoAddComponent] Error al cargar tipos:', err);
        this.error = 'No se pudieron cargar los tipos de evento.';
      }
    });
  }

  onSubmit() {
    if (this.form.invalid) {
      console.warn('[EventoAddComponent] Formulario inválido al enviar');
      return;
    }

    const dto: EventoRequestDto = this.form.value;

    this.eventoService.crearEvento(dto).subscribe({
      next: () => {
        alert('Evento creado correctamente');
        this.router.navigate(['/admin/eventos']);
      },
      error: err => {
        console.error('[EventoAddComponent] Error al crear evento:', err);
        this.error = 'No se pudo crear el evento.';
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

    // tipo "HH:mm" → se compara como string
    if (fin < inicio) {
      return { horaFinMenor: true };
    }

    return null;
  }
}
