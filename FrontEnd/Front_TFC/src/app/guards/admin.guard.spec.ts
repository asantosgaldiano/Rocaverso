import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';
import { adminGuard } from './admin.guard';

describe('adminGuard', () => {

  // Helper para ejecutar el guard dentro del contexto de Angular
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => adminGuard(...guardParameters));

  beforeEach(() => {
    // Configura el entorno de pruebas antes de cada test
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    // Comprueba que el guard se crea correctamente
    expect(executeGuard).toBeTruthy();
  });
});
