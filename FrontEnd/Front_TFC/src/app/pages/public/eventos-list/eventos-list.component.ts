import { Component, signal } from '@angular/core';
import { EventosCardComponent } from "../../../components/public/eventos-card/eventos-card.component";
import { EventoResponseDto } from '../../../interfaces/evento-response-dto';
import { EventoService } from '../../../services/evento.service';

@Component({
  selector: 'app-eventos-list',
  standalone: true,
  imports: [EventosCardComponent],
  templateUrl: './eventos-list.component.html',
  styleUrl: './eventos-list.component.css'
})

export class EventosListComponent {
  eventos = signal<EventoResponseDto[]>([]);

  constructor(private eventoService: EventoService) {

    this.eventoService.getActivos().subscribe({
      next: (data) => {
        this.eventos.set(data);
      },
      error: (err) => console.error('Error cargando eventos', err),
    });
  }
}
