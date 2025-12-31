import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../enviroments/enviroment';
import { UsuarioRequestDto } from '../interfaces/usuario-request-dto';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  
  private apiUrl = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient) {}

  altaAdmin(admin: UsuarioRequestDto): Observable<any> {
    return this.http.post(`${this.apiUrl}/alta/admin`, admin);
  }

  altaMonitor(monitor: UsuarioRequestDto): Observable<any> {
    return this.http.post(`${this.apiUrl}/alta/monitor`, monitor);
  }

  deshabilitarUsuario(email: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/deshabilitar/${email}`, {});
  }

  habilitarUsuario(email: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/habilitar/${email}`, {});
  }
}
