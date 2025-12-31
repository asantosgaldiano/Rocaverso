import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { ViaRequestDto } from '../../../../interfaces/via-request-dto';
import { ViaService } from '../../../../services/via.service';

@Component({
  selector: 'app-via-add',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './via-add.component.html',
  styleUrl: './via-add.component.css'
})

export class ViaAddComponent implements OnInit {
  form!: FormGroup;
  error: string | null = null;

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private viaService = inject(ViaService);

  tipos: string[] = [
    'DEPORTIVA',
    'BOULDER',
    'TRAVESIAS',
    'MOONBOARD'
  ];
  dificultades: string[] = [
    'INICIACION',
    'FACIL',
    'INTERMEDIO',
    'AVANZADO',
    'EXPERTO',
    'PROFESIONAL'
  ];
  ubicaciones: string[] = [
    'DEPORTIVA',
    'BOULDER',
    'ENTRENAMIENTO',
    'INFANTIL',
    'EXTERIOR'
  ];

  ngOnInit(): void {
    this.form = this.fb.group({
      tipo: ['', Validators.required],
      dificultad: ['', Validators.required],
      ubicacion: ['', Validators.required],
      estado: ['ACTIVA'],
      idRocodromo: [1, Validators.required] // Por defecto rocódromo 1
    });
  }

  onSubmit() {
    if (this.form.invalid) {
      return;
    }

    const dto: ViaRequestDto = this.form.value;

    this.viaService.crear(dto).subscribe({
      next: () => {
        alert('Vía creada correctamente');
        this.router.navigate(['/admin/vias']); // ruta a la lista de vías
      },
      error: err => {
        console.error('[ViaAddComponent] Error al crear vía:', err);
        this.error = 'No se pudo crear la vía.';
      }
    });
  }

  checkControl(controlName: string, errorName: string): boolean {
    const control = this.form.get(controlName);
    return !!(control && control.hasError(errorName) && control.touched);
  }

}
