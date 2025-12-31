import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

// EXPLICADO EN admin.guard - Mismo patrón
export const usuarioGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.isAuthenticated() && auth.getRole() === 'USUARIO') {
    return true;
  }

  return router.createUrlTree(['/login']);
};
