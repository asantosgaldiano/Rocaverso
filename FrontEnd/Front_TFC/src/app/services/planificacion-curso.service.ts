import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PlanificacionCursoResponseDto } from '../interfaces/planificacion-curso-response-dto';
import { HttpClient } from '@angular/common/http';
import { environment } from '../enviroments/enviroment';
import { PlanificacionCursoRequestDto } from '../interfaces/planificacion-curso-request-dto';

@Injectable({
  providedIn: 'root'
})

export class PlanificacionCursoService {

  private apiUrl = `${environment.apiUrl}/planificacion`;
  private http = inject(HttpClient);
  
  constructor() { }

  getTodas(): Observable<PlanificacionCursoResponseDto[]> {
    return this.http.get<PlanificacionCursoResponseDto[]>(`${this.apiUrl}/all`);
  }

  getDetalle(id: number): Observable<PlanificacionCursoResponseDto> {
    return this.http.get<PlanificacionCursoResponseDto>(`${this.apiUrl}/detail/${id}`);
  }

  getPorCurso(idCurso: number): Observable<PlanificacionCursoResponseDto[]> {
    return this.http.get<PlanificacionCursoResponseDto[]>(`${this.apiUrl}/curso/${idCurso}`);
  }

  crear(planificacion: PlanificacionCursoRequestDto): Observable<PlanificacionCursoResponseDto> {
    return this.http.post<PlanificacionCursoResponseDto>(`${this.apiUrl}/add`, planificacion);
  }

  editar(id: number, planificacion: PlanificacionCursoRequestDto): Observable<PlanificacionCursoResponseDto> {
    return this.http.put<PlanificacionCursoResponseDto>(`${this.apiUrl}/edit/${id}`, planificacion);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }
  
}
