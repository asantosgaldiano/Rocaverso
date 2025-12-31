import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { MisInscripcionesResponseDto } from '../../../interfaces/mis-inscripciones-response-dto';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-mis-inscripciones-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mis-inscripciones-card.component.html',
  styleUrl: './mis-inscripciones-card.component.css'
})

export class MisInscripcionesCardComponent implements OnInit {

  // Signal que almacena la lista de inscripciones del usuario
  inscripciones = signal<MisInscripcionesResponseDto[]>([]);

  // Inyecto el router para poder navegar a la página del curso
  private router = inject(Router);

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    // Llamo al backend para obtener las inscripciones del usuario
    this.usuarioService.obtenerMisInscripciones().subscribe({
      next: (data) => this.inscripciones.set(data), // Guardo los datos en el signal
      error: (err) => console.error(err)            // Muestro error si la petición falla
    });
  }

  verDetalle(idCurso: number) {
    // Navego a la página de detalle del curso
    this.router.navigate(['/curso', idCurso]);
  }
}

