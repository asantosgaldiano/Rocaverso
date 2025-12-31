import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { EventoResponseDto } from '../../../interfaces/evento-response-dto';
import { EventoService } from '../../../services/evento.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';
import { ReservaService } from '../../../services/reserva.service';
import { UsuarioService } from '../../../services/usuario.service';
import { EventoBotoneraComponent } from "../../../components/public/evento-botonera/evento-botonera.component";

@Component({
  selector: 'app-evento-detail',
  standalone: true,
  imports: [RouterModule, CommonModule, EventoBotoneraComponent],
  templateUrl: './evento-detail.component.html',
  styleUrl: './evento-detail.component.css'
})

export class EventoDetailComponent {
  route = inject(ActivatedRoute);
  eventoService = inject(EventoService);
  usuarioService = inject(UsuarioService);
  reservaService = inject(ReservaService);
  auth = inject(AuthService);

  evento!: EventoResponseDto;
  cargando = true;

  constructor() {
    const idEvento = Number(this.route.snapshot.paramMap.get('idEvento'));
    if (isNaN(idEvento)) {
      console.error('ID no válido');
      return;
    }

    this.eventoService.getDetalle(idEvento).subscribe({
      next: (data) => {
        this.evento = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando evento:', err);
        this.cargando = false;
      }
    });
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
