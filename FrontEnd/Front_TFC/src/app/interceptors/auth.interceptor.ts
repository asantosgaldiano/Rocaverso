import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Accedo al servicio de autenticación
  const authService = inject(AuthService);

  // Obtengo el token almacenado (si existe)
  const token = authService.getToken();

  // Si hay token, clono la petición y le añado el header Authorization
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
    return next(authReq); // Envío la petición modificada
  }
  
  // Si no hay token, dejo pasar la petición tal cual
  return next(req);
};
