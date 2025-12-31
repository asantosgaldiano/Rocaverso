import { Component, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { RegistroRequestDto } from '../../interfaces/registro-request-dto';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { RegistroResponseDto } from '../../interfaces/registro-response-dto';
import { UsuarioService } from '../../services/usuario.service';
import { map, catchError, of } from 'rxjs';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})

export class RegisterComponent {
  form!: FormGroup;
  error: string | null = null;
  loading = false;
  mostrarPassword = false;

  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private usuarioService = inject(UsuarioService);

  ngOnInit() {
    this.createForm();
  }

  createForm() {
    this.form = this.fb.group(
      {
        email: ['', {
          validators: [Validators.required, Validators.email],
          asyncValidators: [this.validarEmailUnico()],
          updateOn: 'blur'
        }],
        nombre: ['', Validators.required],
        apellidos: ['', Validators.required],
        password: ['', [Validators.required, Validators.minLength(6)]],
        repetirPassword: ['', Validators.required],
      },
      { validators: this.passwordsIgualesValidator }
    );
  }

  validarEmailUnico() {
    return (control: AbstractControl) => {
      const email = control.value;
      if (!email) return of(null);

      return this.usuarioService.getDetalle(email).pipe(
        map(usuario => usuario ? { emailEnUso: true } : null),
        catchError(() => of(null))
      );
    };
  }

  passwordsIgualesValidator(group: AbstractControl): ValidationErrors | null {
    const pass = group.get('password')?.value;
    const repetir = group.get('repetirPassword')?.value;
    return pass && repetir && pass !== repetir
      ? { passwordMismatch: true }
      : null;
  }

  toggleMostrarPassword(): void {
    this.mostrarPassword = !this.mostrarPassword;
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = null;

    const dto: RegistroRequestDto = {
      usuario: {
        email: this.form.value.email,
        nombre: this.form.value.nombre,
        apellidos: this.form.value.apellidos,
        password: this.form.value.password,
      },
    };

    this.authService.register(dto).subscribe({
      next: (res: RegistroResponseDto) => {
        this.authService.saveToken(res.token);
        this.authService.saveRole(res.usuario.rol || '');
        const rol = res.usuario.rol;

        switch (rol) {
          case 'USUARIO':
            this.router.navigate(['/usuario/']);
            break;
          case 'MONITOR':
            this.router.navigate(['/monitor/']);
            break;
          case 'ADMON':
            this.router.navigate(['/admin']);
            break;
          default:
            this.router.navigate(['']);
            break;
        }
      },
      error: (err: any) => {
        console.error('Error en el registro:', err);

        if (err.status === 401) {
          this.error = 'Los datos no son válidos.';
        } else {
          this.error = 'Error en el registro. Inténtalo más tarde.';
        }

        this.loading = false;
      },
  
      complete: () => {
        this.loading = false;
      },
    });
  }
}