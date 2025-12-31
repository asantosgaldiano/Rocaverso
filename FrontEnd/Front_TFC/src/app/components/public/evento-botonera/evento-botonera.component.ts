import { Component, computed, inject, Input, signal, SimpleChanges } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { EventoResponseDto } from '../../../interfaces/evento-response-dto';
import { AuthService } from '../../../services/auth.service';
import { ReservaService } from '../../../services/reserva.service';
import { UsuarioService } from '../../../services/usuario.service';
import { EventoService } from '../../../services/evento.service';


@Component({
  selector: 'app-evento-botonera',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './evento-botonera.component.html',
  styleUrl: './evento-botonera.component.css'
})

export class EventoBotoneraComponent {

  // Recibo el evento desde el componente padre
  @Input() evento!: EventoResponseDto;

  // Inyección de servicios necesarios
  auth = inject(AuthService);
  router = inject(Router);
  usuarioService = inject(UsuarioService);
  reservaService = inject(ReservaService);
  eventoService = inject(EventoService);

  // Indica si el usuario ya reservó este evento
  yaReservado = false;

  ngOnInit(): void {
    // Uso un pequeño delay para asegurar que @Input evento esté definido
    setTimeout(() => {
      // Solo los usuarios comprueban si ya reservaron
      if (this.evento && this.auth.getRole() === 'USUARIO') {
        this.comprobarReserva();
      }
    });
  }

  comprobarReserva() {
    // Consulto las reservas del usuario y verifico si coincide este evento
    this.usuarioService.obtenerMisReservas().subscribe({
      next: (reservas) => {
        const existe = reservas.some(r => r.idEvento === this.evento.idEvento);
        this.yaReservado = existe;
      },
      error: () => {
        console.warn('No se pudo comprobar si ya está reservado');
      }
    });
  }

  editar() {
    // Navego al formulario de edición del evento (solo admin)
    this.router.navigate(['/admin/eventos/editar', this.evento.idEvento]);
  }

  reservar() {
    // Llamo al backend para registrar la reserva del usuario
    this.usuarioService.reservarEvento(this.evento.idEvento).subscribe({
      next: () => {
        alert('Reserva realizada correctamente');
        this.yaReservado = true;
      },
      error: (err) => {
        // Manejo específico según error devuelto por el backend
        if (err.status === 409) {
          alert('Ya estás inscrito en este evento');
          this.yaReservado = true;
        } else if (err.status === 400) {
          alert('Aforo completo para este evento');
        } else {
          alert('Error inesperado al reservar el evento');
        }
      }
    });
  }

  cancelarReserva() {
    // Llamada al backend para borrar la reserva
    this.reservaService.cancelarReserva(this.evento.idEvento).subscribe({
      next: () => {
        alert('Reserva cancelada correctamente');
        this.yaReservado = false;
      },
      error: (err) => {
        // Manejo de errores detallado
        if (err.status === 404) {
          alert('No se encontró la reserva');
        } else if (err.status === 500) {
          alert('Error del servidor al cancelar la reserva. Inténtalo más tarde.');
        } else {
          alert('Error inesperado al cancelar la reserva');
        }
      }
    });
  }
  
  eliminar() {
    // Confirmación antes de borrar un evento
    const confirmado = confirm('¿Estás seguro de que deseas eliminar este evento? Esta acción no se puede deshacer.');

    if (!confirmado) return;

    // Llamada al backend para eliminar el evento
    this.eventoService.eliminarEvento(this.evento.idEvento).subscribe({
      next: () => {
        alert('Evento eliminado correctamente');
        // Regreso al listado
        this.router.navigate(['/admin/eventos']);
      },
      error: (err) => {
        console.error('Error al eliminar el evento:', err);
        if (err.status === 404) {
          alert('Evento no encontrado. Puede que ya haya sido eliminado.');
        } else if (err.status === 403) {
          alert('No tienes permisos para eliminar este evento.');
        } else {
          alert('Ocurrió un error inesperado al intentar eliminar el evento.');
        }
      }
    });
  }

  volverAlPanel() {
    this.router.navigate(['/usuario']);
  }
}
