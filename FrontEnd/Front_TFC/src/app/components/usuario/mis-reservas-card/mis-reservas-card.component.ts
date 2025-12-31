import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { MisReservasResponseDto } from '../../../interfaces/mis-reservas-response-dto';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-mis-reservas-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mis-reservas-card.component.html',
  styleUrl: './mis-reservas-card.component.css'
})
export class MisReservasCardComponent implements OnInit {

  reservas = signal<MisReservasResponseDto[]>([]);
  private router = inject(Router);
  
  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.usuarioService.obtenerMisReservas().subscribe({
      next: (data) => this.reservas.set(data),
      error: (err) => console.error(err)
    });
  }

  verDetalle(idEvento: number) {
    this.router.navigate(['/evento', idEvento]);
  }
}
