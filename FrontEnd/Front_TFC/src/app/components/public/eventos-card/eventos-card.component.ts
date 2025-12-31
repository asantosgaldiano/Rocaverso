import { Component, inject, Input } from '@angular/core';
import { CommonModule, CurrencyPipe, NgClass } from '@angular/common';
import { EventoResponseDto } from '../../../interfaces/evento-response-dto';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { ReservaService } from '../../../services/reserva.service';
import { UsuarioService } from '../../../services/usuario.service';

@Component({
  selector: 'app-eventos-card',
  standalone: true,
  imports: [CurrencyPipe, NgClass, RouterModule, CommonModule],
  templateUrl: './eventos-card.component.html',
  styleUrl: './eventos-card.component.css'
})

export class EventosCardComponent {
  @Input() evento!: EventoResponseDto;


  auth = inject(AuthService);
  router = inject(Router);
  usuarioService = inject(UsuarioService);
  reservaService = inject(ReservaService);

  verDetalle() {
    this.router.navigate(['/evento', this.evento.idEvento]);
  }

  get aforo(): string {
    const ratio = this.evento.plazasLibres / this.evento.aforoMaximo;

    if (ratio === 0) {
      return 'bg-danger';       // Sin plazas libres: rojo
    } else if (ratio <= 0.25) {
      return 'bg-warning text-dark';  // Poco aforo: amarillo con texto oscuro
    } else {
      return 'bg-success';      // Suficiente aforo: verde
    }
  }


}
