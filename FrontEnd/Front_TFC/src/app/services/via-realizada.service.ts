import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../enviroments/enviroment';
import { ViaRealizadaRequestDto } from '../interfaces/via-realizada-request-dto';
import { ViaRealizadaResponseDto } from '../interfaces/via-realizada-response-dto';
import { ViaTopResponseDto } from '../interfaces/via-top-response-dto';
import { UsuarioTopResponseDto } from '../interfaces/usuario-top-response-dto';

@Injectable({
  providedIn: 'root'
})

export class ViaRealizadaService {

  private apiUrl = `${environment.apiUrl}/viarealizada`;
  private http = inject(HttpClient);

  constructor() {}

  getMisVias(): Observable<ViaRealizadaResponseDto[]> {
    return this.http.get<ViaRealizadaResponseDto[]>(`${this.apiUrl}/misvias`);
  }

  addViaRealizada(request: ViaRealizadaRequestDto): Observable<ViaRealizadaResponseDto> {
    return this.http.post<ViaRealizadaResponseDto>(`${this.apiUrl}/add`, request);
  }

  updateViaRealizada(id: number, request: ViaRealizadaRequestDto): Observable<ViaRealizadaResponseDto> {
    return this.http.put<ViaRealizadaResponseDto>(`${this.apiUrl}/edit/${id}`, request);
  }

  deleteViaRealizada(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }

  getTop5Vias(): Observable<ViaTopResponseDto[]> {
    return this.http.get<ViaTopResponseDto[]>(`${this.apiUrl}/top5`);
  }

  getTop3Usuarios(): Observable<UsuarioTopResponseDto[]> {
    return this.http.get<UsuarioTopResponseDto[]>(`${this.apiUrl}/podio3`);
  }

}


