import { Component, OnInit, signal } from '@angular/core';
import { ViaRealizadaResponseDto } from '../../../interfaces/via-realizada-response-dto';
import { ViaRealizadaService } from '../../../services/via-realizada.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-vias-realizadas-card',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './vias-realizadas-card.component.html',
  styleUrl: './vias-realizadas-card.component.css'
})
export class ViasRealizadasCardComponent implements OnInit {

  vias = signal<ViaRealizadaResponseDto[]>([]);

  constructor(private viaRealizadaService: ViaRealizadaService) {}

  ngOnInit(): void {
    this.cargarVias();
  }

  cargarVias() {
    this.viaRealizadaService.getMisVias().subscribe({
      next: (data) => this.vias.set(data),
      error: (err) => {
        console.error('Error al cargar vías realizadas:', err);
        this.vias.set([]);
      }
    });
  }
}
