import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

// EXPLICADO EN admin.guard - Mismo patrón
export const monitorGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.isAuthenticated() && auth.getRole() === 'MONITOR') {
    return true;
  }

  return router.createUrlTree(['/login']);
};
