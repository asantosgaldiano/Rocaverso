import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';


export const adminGuard: CanActivateFn = () => {
  // Obtengo el servicio de autenticación
  const auth = inject(AuthService);

  // Necesito el Router para redirigir si no tiene permisos
  const router = inject(Router);

  // Si está autenticado y su rol es ADMIN (ADMON), permito acceso
  if (auth.isAuthenticated() && auth.getRole() === 'ADMON') {
    return true;
  }

  // Si no cumple, lo mando al login
  return router.createUrlTree(['/login']);
};
