import { Component, signal } from '@angular/core';
import { CursoResponseDto } from '../../../interfaces/curso-response-dto';
import { CursoService } from '../../../services/curso.service';
import { CursosCardComponent } from '../../../components/public/cursos-card/cursos-card.component';

@Component({
  selector: 'app-cursos-list',
  standalone: true,
  imports: [CursosCardComponent],
  templateUrl: './cursos-list.component.html',
  styleUrl: './cursos-list.component.css'
})

export class CursosListComponent {
  cursos = signal<CursoResponseDto[]>([]);

  constructor(private cursoService: CursoService) {
    this.cursoService.getActivos().subscribe({
      next: (data) => {
        this.cursos.set(data);
      },
      error: (err) => console.error('Error cargando cursos', err),
    });
  }
}
