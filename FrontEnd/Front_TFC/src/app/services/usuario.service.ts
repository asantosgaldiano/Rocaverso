import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../enviroments/enviroment';
import { EstadisticasUsuarioDto } from '../interfaces/estadisticas-usuario-dto';
import { MisInscripcionesResponseDto } from '../interfaces/mis-inscripciones-response-dto';
import { MisReservasResponseDto } from '../interfaces/mis-reservas-response-dto';
import { UsuarioRequestDto } from '../interfaces/usuario-request-dto';
import { UsuarioResponseDto } from '../interfaces/usuario-response-dto';
import { MisInscripcionesCalendarDto } from '../interfaces/mis-inscripciones-calendar-dto';

@Injectable({
  providedIn: 'root'
})

export class UsuarioService {

  private apiUrl = `${environment.apiUrl}/usuario`;
  private http = inject(HttpClient);

  constructor() { }

  getTodos(): Observable<UsuarioResponseDto[]> {
    return this.http.get<UsuarioResponseDto[]>(`${this.apiUrl}/all`);
  }

  getDetalle(email: string): Observable<UsuarioResponseDto> {
    return this.http.get<UsuarioResponseDto>(`${this.apiUrl}/detail/${email}`);
  }

  editar(email: string, usuario: Partial<UsuarioRequestDto>): Observable<any> {
    return this.http.put(`${this.apiUrl}/edit/${email}`, usuario);
  }

  eliminar(email: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${email}`);
  }

  // Endpoints para el usuario autenticado
  obtenerMiPerfil(): Observable<UsuarioResponseDto> {
    return this.http.get<UsuarioResponseDto>(`${this.apiUrl}/miperfil`);
  }

  editarMiPerfil(usuario: Partial<UsuarioRequestDto>): Observable<UsuarioResponseDto> {
    return this.http.put<UsuarioResponseDto>(`${this.apiUrl}/miperfil/edit`, usuario);
  }

  reservarEvento(idEvento: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/reservarevento/${idEvento}`, null);
  }

  obtenerMisReservas(): Observable<MisReservasResponseDto[]> {
    return this.http.get<MisReservasResponseDto[]>(`${this.apiUrl}/misreservas`);
  }

  inscribirseCurso(idCurso: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/inscribirsecurso/${idCurso}`, null);
  }

  obtenerMisInscripciones(): Observable<MisInscripcionesResponseDto[]> {
    return this.http.get<MisInscripcionesResponseDto[]>(`${this.apiUrl}/misinscripciones`);
  }
  
  obtenerMisInscripcionesCalendar(): Observable<MisInscripcionesCalendarDto[]> {
    return this.http.get<MisInscripcionesCalendarDto[]>(`${this.apiUrl}/misinscripcionescalendar`);
  }
  obtenerMisEstadisticas(): Observable<EstadisticasUsuarioDto> {
    return this.http.get<EstadisticasUsuarioDto>(`${this.apiUrl}/misestadisticas`);
  }
}
