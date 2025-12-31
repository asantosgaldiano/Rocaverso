import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { monitorGuard } from './monitor.guard';

// EXPLICADO EN admin.guard - Mismo patrón
describe('monitorGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => monitorGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
