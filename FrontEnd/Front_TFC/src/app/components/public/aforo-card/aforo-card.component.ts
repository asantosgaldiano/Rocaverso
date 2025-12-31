import { Component, computed, inject, Input, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AforoActualDto } from '../../../interfaces/aforo-actual-dto';
import { AforoService } from '../../../services/aforo.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'app-aforo-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './aforo-card.component.html',
  styleUrl: './aforo-card.component.css'
})

export class AforoCardComponent {

  /** 
   * Signal que almacena el estado actual del aforo (ocupación, máximo, porcentaje).
   * Uso signal() para reaccionar automáticamente a los cambios de valor.
  */
  aforo = signal<AforoActualDto>({
    aforoMaximo: 0,
    usuariosDentro: 0,
    porcentajeOcupado: 0,
  });

  aforoService: AforoService = inject(AforoService);

  /** 
   * DomSanitizer permite marcar HTML como “seguro” para que Angular lo renderice.
   * Sin esto, Angular bloquearía el SVG insertado dinámicamente por seguridad.
  */
  sanitizer = inject(DomSanitizer);

  constructor() {
    this.aforoService.getAforoActual().subscribe({
      next: (data) => {
        this.aforo.set(data);
      },
      error: (err) => console.error('Error cargando aforo', err),
    });
  }

  /** Devuelve el color del trazo según el porcentaje */
  get strokeColor(): string {
    const p = this.aforo().porcentajeOcupado;
    if (p >= 80) return '#dc3545';
    if (p >= 50) return '#ffc107';
    return '#ffce00';
  }

  /** Devuelve el SVG como SafeHtml */
    /**
   * Genera dinámicamente un SVG que representa el porcentaje de ocupación.
   * 
   * - `dasharray`: Longitud total del perímetro del círculo.
   * - `dashoffset`: Cuánto del círculo queda “sin colorear” (para simular el progreso).
   * 
   * Se usa computed() para que el SVG se regenere automáticamente
   * cada vez que cambie el valor del signal `aforo`.
   */
  svg = computed<SafeHtml>(() => {
    const p = this.aforo().porcentajeOcupado; // porcentaje actual
    const color = this.strokeColor;            // color según nivel de ocupación
    const dasharray = 408;                     // longitud perímetro del círculo en el SVG
    const dashoffset = dasharray - (dasharray * p) / 100; // calcula el “vacío” del círculo

    // SVG generado dinámicamente con interpolación de variables de TypeScript
    const rawSvg = `
      <svg width="150" height="150">
        <!-- Círculo gris de fondo -->
        <circle cx="75" cy="75" r="65" stroke="#e9ecef" stroke-width="12" fill="none"></circle>

        <!-- Círculo de progreso (color dinámico) -->
          <!-- extremos redondeados -->
          <!-- longitud total del trazo -->
          <!-- parte “vacía” según porcentaje -->
          <!-- gira el círculo para empezar desde arriba -->
        <circle cx="75" cy="75" r="65" stroke="${color}" stroke-width="12" fill="none"
          stroke-linecap="round"                      
          stroke-dasharray="${dasharray}"             
          stroke-dashoffset="${dashoffset}"           
          transform="rotate(-90 75 75)">              
        </circle>

        <!-- Texto centrado que muestra el porcentaje -->
        <text x="50%" y="50%" text-anchor="middle" dy=".3em"
          style="font-weight:600; font-size:1.5rem; font-family:'Poppins',sans-serif; fill:#000;">
          ${p}%
        </text>
      </svg>
    `;

    /**
     * Como el SVG es un string de HTML generado manualmente, Angular lo consideraría inseguro.
     * Usamos `bypassSecurityTrustHtml` para indicarle a Angular que este contenido es seguro
     * y que puede insertarlo en el DOM mediante [innerHTML].
    */
    return this.sanitizer.bypassSecurityTrustHtml(rawSvg);
  });
}
