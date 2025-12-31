import { Component, inject } from '@angular/core';
import { ViaResponseDto } from '../../../interfaces/via-response-dto';
import { ViaService } from '../../../services/via.service';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-vias',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-vias.component.html',
  styleUrl: './admin-vias.component.css'
})

export class AdminViasComponent {
  vias: ViaResponseDto[] = [];

  private viaService = inject(ViaService);
  private router = inject(Router);

  ngOnInit() {
    this.viaService.getTodas().subscribe({
      next: (data) => this.vias = data,
      error: (err) => console.error('Error cargando vías', err),
    });
  }

  activar(id: number) {
    const confirmacion = confirm('¿Estás seguro de que deseas activar esta vía?');
    if (confirmacion) {
      this.viaService.activar(id).subscribe({
        next: () => this.recargarVias(),
        error: () => alert('No se pudo activar la vía.'),
      });
    }
  }

  desactivar(id: number) {
    const confirmacion = confirm('¿Estás seguro de que deseas desactivar esta vía?');
    if (confirmacion) {
      this.viaService.desactivar(id).subscribe({
        next: () => this.recargarVias(),
        error: () => alert('No se pudo desactivar la vía.'),
      });
    }
  }

  eliminar(id: number) {
    const confirmacion = confirm('¿Deseas eliminar esta vía permanentemente?');
    if (confirmacion) {
      this.viaService.eliminar(id).subscribe({
        next: () => this.recargarVias(),
        error: () => alert('No se pudo eliminar la vía.'),
      });
    }
  }

  recargarVias() {
    this.viaService.getTodas().subscribe({
      next: (data) => this.vias = data,
      error: (err) => console.error('Error recargando vías', err),
    });
  }

  volverAlPanel() {
    this.router.navigate(['/admin']);
  }
}
