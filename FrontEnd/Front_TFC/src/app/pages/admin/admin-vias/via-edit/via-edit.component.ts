import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { ViaRequestDto } from '../../../../interfaces/via-request-dto';
import { ViaService } from '../../../../services/via.service';
import { ViaResponseDto } from '../../../../interfaces/via-response-dto';

@Component({
  selector: 'app-via-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './via-edit.component.html',
  styleUrl: './via-edit.component.css'
})

export class ViaEditComponent implements OnInit {

  form!: FormGroup;
  idVia!: number;
  error: string | null = null;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private viaService = inject(ViaService);

  tipos: string[] = ['DEPORTIVA', 'BOULDER', 'TRAVESIAS', 'MOONBOARD'];
  dificultades: string[] = ['INICIACION', 'FACIL', 'INTERMEDIO', 'AVANZADO', 'EXPERTO', 'PROFESIONAL'];
  ubicaciones: string[] = ['DEPORTIVA', 'BOULDER', 'ENTRENAMIENTO', 'INFANTIL', 'EXTERIOR'];

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('idVia');
      if (!idParam) {
        this.error = 'ID de vía no válido';
        return;
      }
      this.idVia = +idParam;

      this.form = this.fb.group({
        tipo: ['', Validators.required],
        dificultad: ['', Validators.required],
        ubicacion: ['', Validators.required],
        estado: ['ACTIVA'], 
        idRocodromo: [1, Validators.required] // fija 1 
      });

      this.viaService.getDetalle(this.idVia).subscribe({
        next: (via: ViaResponseDto) => {
          this.form.patchValue({
            tipo: via.tipo,
            dificultad: via.dificultad,
            ubicacion: via.ubicacion,
            estado: via.estado,
            idRocodromo: 1
          });
        },
        error: err => {
          this.error = 'No se pudo cargar la vía.';
          console.error('[ViaEditComponent] Error al obtener vía:', err);
        }
      });
    });
  }

  onSubmit() {
    if (this.form.invalid) {
      console.warn('[ViaEditComponent] Formulario inválido');
      return;
    }
    const dto: ViaRequestDto = this.form.value;

    this.viaService.editar(this.idVia, dto).subscribe({
      next: () => {
        alert('Vía actualizada correctamente');
        this.router.navigate(['/admin/vias']);
      },
      error: err => {
        this.error = 'No se pudo actualizar la vía.';
        console.error('[ViaEditComponent] Error al actualizar vía:', err);
      }
    });
  }

}
