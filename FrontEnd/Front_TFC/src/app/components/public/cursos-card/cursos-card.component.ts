import { CommonModule, CurrencyPipe, NgClass } from '@angular/common';
import { Component, inject, Input } from '@angular/core';
import { CursoBotoneraComponent } from "../curso-botonera/curso-botonera.component";
import { CursoResponseDto } from '../../../interfaces/curso-response-dto';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-cursos-card',
  standalone: true,
  imports: [CurrencyPipe, NgClass, RouterModule, CommonModule],
  templateUrl: './cursos-card.component.html',
  styleUrl: './cursos-card.component.css'
})
export class CursosCardComponent {
  @Input() curso!: CursoResponseDto;

  auth = inject(AuthService);
  router = inject(Router);
  
  verDetalle() {
    console.log('verDetalle() llamado');
    this.router.navigate(['/curso', this.curso.idCurso]);
  }

  get badgeAforoClass(): string {
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
