import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from '../enviroments/enviroment';
import { CursoResponseDto } from '../interfaces/curso-response-dto';
import { CursoRequestDto } from '../interfaces/curso-request-dto';
import { PlanificacionCursoRequestDto } from '../interfaces/planificacion-curso-request-dto';

@Injectable({
  providedIn: 'root'
})
export class CursoService {

  private apiUrl = `${environment.apiUrl}/curso`;
  private http: HttpClient = inject(HttpClient);
  private router: Router = inject(Router);

  constructor() { }

  getTodas(): Observable<CursoResponseDto[]> {
    return this.http.get<CursoResponseDto[]>(`${this.apiUrl}/all`);
  }

  getActivos(): Observable<CursoResponseDto[]> {
    return this.http.get<CursoResponseDto[]>(`${this.apiUrl}/activos`);
  }

  getDetalle(id: number): Observable<CursoResponseDto> {
    return this.http.get<CursoResponseDto>(`${this.apiUrl}/detail/${id}`);
  }

  getCursosDelMonitor(): Observable<CursoResponseDto[]> {
    return this.http.get<CursoResponseDto[]>(`${this.apiUrl}/miscursos`);
  }
  
  crear(curso: CursoRequestDto): Observable<CursoResponseDto> {
    return this.http.post<CursoResponseDto>(`${this.apiUrl}/add`, curso);
  }

  editar(id: number, curso: CursoRequestDto): Observable<CursoResponseDto> {
    return this.http.put<CursoResponseDto>(`${this.apiUrl}/edit/${id}`, curso);
  }

  cancelar(idCurso: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/cancelar/${idCurso}`, null);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  terminarCurso(idCurso: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/terminar/${idCurso}`, null);
  }

  aceptarCurso(idCurso: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/aceptar/${idCurso}`, null);
  }

  addPlanificacionCurso(idCurso: number, planificaciones: PlanificacionCursoRequestDto[]): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/addplanificacioncurso/${idCurso}`, planificaciones);
  }

  getCursosMonitor(email: string) {
    return this.http.get<CursoResponseDto[]>(`${this.apiUrl}/monitor/${email}`);
  } 
   
}
