import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../enviroments/enviroment';
import { Observable } from 'rxjs';
import { EventoResponseDto } from '../interfaces/evento-response-dto';
import { EventoRequestDto } from '../interfaces/evento-request-dto';

@Injectable({
  providedIn: 'root'
})

export class EventoService {

  private apiUrl = `${environment.apiUrl}/evento`;
  private http: HttpClient = inject(HttpClient);
  private router: Router = inject(Router);

  constructor() { }

  getTodas(): Observable<EventoResponseDto[]> {
    return this.http.get<EventoResponseDto[]>(`${this.apiUrl}/all`);
  }

  getActivos(): Observable<EventoResponseDto[]> {
    return this.http.get<EventoResponseDto[]>(`${this.apiUrl}/activos`);
  }

  getDetalle(id: number): Observable<EventoResponseDto> {
    return this.http.get<EventoResponseDto>(`${this.apiUrl}/detail/${id}`);
  }

  crearEvento(dto: EventoRequestDto): Observable<EventoResponseDto> {
    return this.http.post<EventoResponseDto>(`${this.apiUrl}/add`, dto);
  }

  editarEvento(id: number, dto: EventoRequestDto): Observable<EventoResponseDto> {
    return this.http.put<EventoResponseDto>(`${this.apiUrl}/edit/${id}`, dto);
  }

  cancelarEvento(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/cancelar/${id}`, null);
  }

  eliminarEvento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  aceptarEvento(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/aceptar/${id}`, null);
  }

  terminarEvento(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/terminar/${id}`, null);
  }
  
}
