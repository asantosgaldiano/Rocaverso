import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { EventoResponseDto } from '../../../interfaces/evento-response-dto';
import { EventoService } from '../../../services/evento.service';
import { ReservaService } from '../../../services/reserva.service';
import { ReservaResponseDto } from '../../../interfaces/reserva-response-dto';

@Component({
  selector: 'app-admin-eventos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-eventos.component.html',
  styleUrl: './admin-eventos.component.css'
})

export class AdminEventosComponent {
  eventos: EventoResponseDto[] = [];
  reservasPorEvento: Record<number, number> = {};  // Guarda nº de reservas por idEvento

  private eventoService = inject(EventoService);
  private reservaService = inject(ReservaService);
  private router = inject(Router);
  
  ngOnInit() {
    // Carga inicial de eventos
    this.eventoService.getTodas().subscribe({
      next: (data) => {
        this.eventos = data;
        this.cargarReservasEventos();   // Después de cargar eventos, cargo sus reservas
      },
      error: (err) => console.error('Error cargando eventos', err),
    });
  }

  cargarReservasEventos() {
    // Recorre cada evento y obtiene su nº de reservas
    this.eventos.forEach(evento => {
      this.reservaService.getReservasByEvento(evento.idEvento).subscribe({
        next: (reservas: ReservaResponseDto[]) => {
          this.reservasPorEvento[evento.idEvento] = reservas.length;
        },
        error: () => {
          this.reservasPorEvento[evento.idEvento] = 0;   // Si falla, pongo 0
        }
      });
    });
  }

  esMinimoAsistenciaAlcanzado(evento: EventoResponseDto): boolean {
    // Comprueba si el evento cumple el mínimo requerido
    const reservas = this.reservasPorEvento[evento.idEvento] || 0;
    return reservas >= evento.minimoAsistencia;
  }

  cancelar(id: number) {
    const evento = this.eventos.find(e => e.idEvento === id);

    if (!evento) {
      alert('Evento no encontrado');
      return;
    }

    if (evento.estado === 'CANCELADO') {
      alert('Este evento ya está cancelado.');
      return;
    }

    const confirmado = confirm('¿Estás seguro de que deseas cancelar este evento?');

    if (confirmado) {
      this.eventoService.cancelarEvento(id).subscribe({
        next: () => {
          alert('Evento cancelado correctamente');

          // Recarga la lista completa para actualizar estado + reservas
          this.eventoService.getTodas().subscribe({
            next: (data) => {
              this.eventos = data;
              this.cargarReservasEventos();   // Actualiza reservas del listado
            },
            error: (err) => console.error('Error recargando eventos después de cancelar', err),
          });
        },
        error: (err) => {
          console.error('Error al cancelar evento:', err);
          alert('No se pudo cancelar el evento.');
        }
      });
    }
  }

  aceptar(id: number) {
    const evento = this.eventos.find(e => e.idEvento === id);

    if (!evento) {
      alert('Evento no encontrado');
      return;
    }

    if (evento.estado === 'ACEPTADO') {
      alert('Este evento ya está aceptado.');
      return;
    }

    const confirmado = confirm('¿Estás seguro de que deseas aceptar este evento?');

    if (confirmado) {
      this.eventoService.aceptarEvento(id).subscribe({
        next: () => {
          alert('Evento aceptado correctamente');

          // Vuelvo a recargar para mostrar el nuevo estado
          this.eventoService.getTodas().subscribe({
            next: (data) => {
              this.eventos = data;
              this.cargarReservasEventos();   // Recalcular reservas
            },
            error: (err) => console.error('Error recargando eventos', err),
          });
        },
        error: (err) => {
          console.error('Error al aceptar evento:', err);
          alert('No se pudo aceptar el evento.');
        }
      });
    }
  }

  terminar(id: number) {
    const evento = this.eventos.find(e => e.idEvento === id);

    if (!evento) {
      alert('Evento no encontrado');
      return;
    }

    if (evento.estado === 'TERMINADO') {
      alert('Este evento ya está terminado.');
      return;
    }

    if (evento.estado === 'CANCELADO') {
      alert('No se puede terminar un evento cancelado.');
      return;
    }

    const confirmado = confirm('¿Estás seguro de que deseas marcar como terminado este evento?');

    if (confirmado) {
      this.eventoService.terminarEvento(id).subscribe({
        next: () => {
          alert('Evento marcado como terminado correctamente');

          // Recargo para mostrar nuevo estado y reservas
          this.eventoService.getTodas().subscribe({
            next: (data) => {
              this.eventos = data;
              this.cargarReservasEventos();
            },
            error: (err) => console.error('Error recargando eventos', err),
          });
        },
        error: (err) => {
          console.error('Error al terminar evento:', err);
          alert('No se pudo terminar el evento.');
        }
      });
    }
  }

  volverAlPanel() {
    this.router.navigate(['/admin']);
  }
}

