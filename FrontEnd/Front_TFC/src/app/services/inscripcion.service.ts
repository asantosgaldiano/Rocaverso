import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../enviroments/enviroment';
import { InscripcionResponseDto } from '../interfaces/inscripcion-response-dto';

@Injectable({
  providedIn: 'root'
})

export class InscripcionService {

  private apiUrl = `${environment.apiUrl}/inscripcion`;
  private http = inject(HttpClient);

  constructor() { }

  getInscripcionesByCurso(idCurso: number): Observable<InscripcionResponseDto[]> {
    return this.http.get<InscripcionResponseDto[]>(`${this.apiUrl}/curso/${idCurso}`);
  }

  cancelarInscripcion(idCurso: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/cancelar/${idCurso}`);
  }
  
}
