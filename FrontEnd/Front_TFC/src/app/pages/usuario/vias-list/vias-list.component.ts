import { CommonModule } from '@angular/common';
import { Component, effect, inject, OnInit, signal, Signal, WritableSignal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { Via } from '../../../interfaces/via';
import { ViaRealizadaRequestDto } from '../../../interfaces/via-realizada-request-dto';
import { ViaRealizadaResponseDto } from '../../../interfaces/via-realizada-response-dto';
import { ViaRealizadaService } from '../../../services/via-realizada.service';
import { ViaService } from '../../../services/via.service';
import { ViaTopResponseDto } from '../../../interfaces/via-top-response-dto';
import { UsuarioTopResponseDto } from '../../../interfaces/usuario-top-response-dto';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-vias-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './vias-list.component.html',
  styleUrl: './vias-list.component.css'
})

export class ViasListComponent {
  private viaService = inject(ViaService);
  private viaRealizadaService = inject(ViaRealizadaService);

  // Signals: almacenan estado reactivo sin necesidad de Observables
  vias = signal<Via[]>([]);
  misViasRealizadas = signal<ViaRealizadaResponseDto[]>([]);
  topVias = signal<ViaTopResponseDto[]>([]);
  topUsuarios = signal<UsuarioTopResponseDto[]>([]); 
  
  // Signals para los filtros (reactivos)
  filtroTipo = signal<string>('TODO');
  filtroDificultad = signal<string>('TODO');
  filtroUbicacion = signal<string>('TODO');

  // Listas para los selects
  tipos = signal<string[]>([]);
  dificultades = signal<string[]>([]);
  ubicaciones = signal<string[]>([]);

  constructor() {
    // Carga inicial
    this.actualizarMisViasRealizadas();
    this.cargarTopVias();
    this.cargarTopUsuarios();
    this.cargarFiltros();

    // effect() se ejecuta automáticamente cuando algún signal usado dentro cambia
    effect(() => {
      // Este efecto recarga el listado cuando cambia un filtro
      this.cargarVias();
    });
  }

  // Revisa si una vía ya fue marcada por el usuario
  isViaRealizada(idVia: number): boolean {
    return this.misViasRealizadas().some(vr => vr.idVia === idVia);
  }

  // Devuelve el id de la vía realizada para poder eliminarla
  getIdViaRealizada(idVia: number): number | undefined {
    return this.misViasRealizadas().find(vr => vr.idVia === idVia)?.idViaRealizada;
  }

  // Carga las vías filtradas
  cargarVias() {
    this.viaService.filtrarVias(
      this.filtroTipo(),
      this.filtroDificultad(),
      this.filtroUbicacion()
    ).subscribe({
      next: vias => this.vias.set(vias),
      error: () => this.vias.set([]) // Si falla, vacío lista
    });
  }

// Carga los datos para los selects de filtro
cargarFiltros() {
  // Primero pido al backend la lista de tipos de vía (deportiva, boulder, etc.)
  this.viaService.getTipos().subscribe({
    // Si la petición va bien, guardo los tipos en el signal 'tipos'
    // Le añado al principio 'TODO' para tener la opción "sin filtro"
    next: tipos => this.tipos.set(['TODO', ...tipos]),
    // Si algo falla en la petición, dejo solo 'TODO' como opción disponible
    error: () => this.tipos.set(['TODO'])
  });

  // Ahora hago lo mismo pero para las dificultades (INICIACION, FACIL, etc.)
  this.viaService.getDificultades().subscribe({
    // Guardo la lista de dificultades con 'TODO' al inicio
    next: dificultades => this.dificultades.set(['TODO', ...dificultades]),
    // Si hay error, al menos mantengo la opción 'TODO'
    error: () => this.dificultades.set(['TODO'])
  });

  // Y por último cargo las ubicaciones (zonas del rocódromo)
  this.viaService.getUbicaciones().subscribe({
    // Guardo las ubicaciones en el signal y añado 'TODO' al principio
    next: ubicaciones => this.ubicaciones.set(['TODO', ...ubicaciones]),
    // En caso de error, solo dejo 'TODO' como opción
    error: () => this.ubicaciones.set(['TODO'])
  });
}


  // Ranking Top 5
  cargarTopVias() {
    this.viaRealizadaService.getTop5Vias().subscribe({
      next: (top) => this.topVias.set(top),
      error: () => this.topVias.set([])
    });
  }

  // Ranking Top 3 usuarios
  cargarTopUsuarios() {
    this.viaRealizadaService.getTop3Usuarios().subscribe({
      next: (usuarios) => this.topUsuarios.set(usuarios),
      error: () => this.topUsuarios.set([])
    });
  }
  
  // Reset de filtros
  resetFiltros() {
    this.filtroTipo.set('TODO');
    this.filtroDificultad.set('TODO');
    this.filtroUbicacion.set('TODO');
  }

  // Marca la vía como realizada
  marcarVia(idVia: number) {
    const request: ViaRealizadaRequestDto = { idVia };
    this.viaRealizadaService.addViaRealizada(request).subscribe({
      next: () => {
        this.actualizarMisViasRealizadas();
        this.cargarTopVias();
        this.cargarTopUsuarios();
      }
    });
  }

  // Borra la marca de vía realizada
  borrarVia(idViaRealizada: number) {

    // UI SÚPER RÁPIDA
    this.misViasRealizadas.update(list =>
      list.filter(v => v.idViaRealizada !== idViaRealizada)
    );

    // BACKEND EN PARALELO
    this.viaRealizadaService.deleteViaRealizada(idViaRealizada).subscribe({
      next: () => {
        this.recargarRankings();
      }
    });
  }

  recargarRankings() {
    forkJoin([
      this.viaRealizadaService.getTop5Vias(),
      this.viaRealizadaService.getTop3Usuarios()
    ]).subscribe(([topVias, topUsuarios]) => {
      this.topVias.set(topVias);
      this.topUsuarios.set(topUsuarios);
    });
  }

  // Recarga mis vías realizadas
  private actualizarMisViasRealizadas() {
    this.viaRealizadaService.getMisVias().subscribe({
      next: (viasRealizadas) => this.misViasRealizadas.set(viasRealizadas),
      error: () => this.misViasRealizadas.set([])
    });
  }
}
