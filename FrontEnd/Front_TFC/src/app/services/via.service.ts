import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../enviroments/enviroment';
import { Via } from '../interfaces/via';
import { ViaRequestDto } from '../interfaces/via-request-dto';
import { ViaResponseDto } from '../interfaces/via-response-dto';

@Injectable({
  providedIn: 'root'
})

export class ViaService {

  private apiUrl = `${environment.apiUrl}/via`;
  private http = inject(HttpClient);

  constructor() { }

  getTodas(): Observable<ViaResponseDto[]> {
    return this.http.get<ViaResponseDto[]>(`${this.apiUrl}/all`);
  }

  getActivas(): Observable<ViaResponseDto[]> {
    return this.http.get<ViaResponseDto[]>(`${this.apiUrl}/activas`);
  }

  getDetalle(idVia: number): Observable<ViaResponseDto> {
    return this.http.get<ViaResponseDto>(`${this.apiUrl}/detail/${idVia}`);
  }

  crear(via: ViaRequestDto): Observable<ViaResponseDto> {
    return this.http.post<ViaResponseDto>(`${this.apiUrl}/add`, via);
  }

  editar(idVia: number, via: ViaRequestDto): Observable<ViaResponseDto> {
    return this.http.put<ViaResponseDto>(`${this.apiUrl}/edit/${idVia}`, via);
  }

  eliminar(idVia: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${idVia}`);
  }

  activar(idVia: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/activar/${idVia}`, null);
  }

  desactivar(idVia: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/desactivar/${idVia}`, null);
  }

  getPorTipo(tipo: string): Observable<ViaResponseDto[]> {
    return this.http.get<ViaResponseDto[]>(`${this.apiUrl}/tipo/${tipo}`);
  }

  getPorDificultad(dificultad: string): Observable<ViaResponseDto[]> {
    return this.http.get<ViaResponseDto[]>(`${this.apiUrl}/dificultad/${dificultad}`);
  }

  getPorUbicacion(ubicacion: string): Observable<ViaResponseDto[]> {
    return this.http.get<ViaResponseDto[]>(`${this.apiUrl}/ubicacion/${ubicacion}`);
  }

  filtrarVias(tipo?: string, dificultad?: string, ubicacion?: string): Observable<ViaResponseDto[]> {

    const params: any = {};

    if (tipo && tipo !== 'TODO') params.tipo = tipo;
    if (dificultad && dificultad !== 'TODO') params.dificultad = dificultad;
    if (ubicacion && ubicacion !== 'TODO') params.ubicacion = ubicacion;

    return this.http.get<ViaResponseDto[]>(`${this.apiUrl}/filtrar`, { params });
    
  }

  getTipos(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/tipos`);
  }

  getDificultades(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/dificultades`);
  }

  getUbicaciones(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/ubicaciones`);
  }

}
