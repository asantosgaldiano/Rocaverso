import { inject, Injectable } from '@angular/core';
import { environment } from '../enviroments/enviroment';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginResponseDto } from '../interfaces/login-response-dto';
import { RegistroResponseDto } from '../interfaces/registro-response-dto';
import { RegistroRequestDto } from '../interfaces/registro-request-dto';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // URL base del backend para autenticación
  private apiUrl = `${environment.apiUrl}/auth`;

  // Inyección manual de dependencias (forma moderna con inject)
  private http: HttpClient = inject(HttpClient);
  private router: Router = inject(Router);

  constructor() { }

  // Petición al backend para hacer login
  login(credentials: { email: string; password: string }): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(`${this.apiUrl}/login`, credentials);
  }

  // Petición para registrar un nuevo usuario
  register(data: RegistroRequestDto): Observable<RegistroResponseDto> {
    return this.http.post<RegistroResponseDto>(`${this.apiUrl}/registro`, data);
  }

  // Guarda el token en localStorage
  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  // Guarda el rol en localStorage
  saveRole(role: string) {
    localStorage.setItem('role', role);
  }

  // Obtiene el token almacenado
  getToken() {
    return localStorage.getItem('token');
  }

  // Obtiene el rol almacenado
  getRole() {
    return localStorage.getItem('role');
  }

  // Limpia sesión y redirige al login
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    this.router.navigate(['/login']);
  }

  // Devuelve true si hay token => usuario autenticado
  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  // redirectByRole(role: string) {
  //   switch (role) {
  //     case 'USUARIO':
  //       this.router.navigate(['/admin']);
  //       break;
  //     case 'MONITOR':
  //       this.router.navigate(['/admin']);
  //       break;
  //     case 'ADMON':
  //       this.router.navigate(['/admin']);
  //       break;
  //     default:
  //       this.router.navigate(['/admin']);
  //   }
  // }  
}
