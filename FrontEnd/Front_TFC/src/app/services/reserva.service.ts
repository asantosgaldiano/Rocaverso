import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../enviroments/enviroment';
import { ReservaResponseDto } from '../interfaces/reserva-response-dto';

@Injectable({
  providedIn: 'root'
})

export class ReservaService {

  private apiUrl = `${environment.apiUrl}/reserva`;
  private http = inject(HttpClient);

  constructor() { }

  getReservasByEvento(idEvento: number): Observable<ReservaResponseDto[]> {
    return this.http.get<ReservaResponseDto[]>(`${this.apiUrl}/evento/${idEvento}`);
  }

  cancelarReserva(idEvento: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/cancelar/${idEvento}`);
  }
  
}
