import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { map, catchError, of, tap } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {
  loginForm!: FormGroup;
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
    this.loginForm = this.fb.group({
      email: ['', {
        validators: [Validators.required, Validators.email],
        asyncValidators: [this.validarEnabledUsuario()],
        updateOn: 'blur'
      }],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  validarEnabledUsuario() {
    return (control: AbstractControl) => {
      const email = control.value;
      if (!email) return of(null);

      return this.usuarioService.getDetalle(email).pipe(
        map(usuario => {
          if (!usuario) return null;
          return usuario.enabled === 0 ? { usuarioDeshabilitado: true } : null;
        }),
        catchError(() => of(null))
      );
    };
  }

  toggleMostrarPassword(): void {
    this.mostrarPassword = !this.mostrarPassword;
  }

  onForgotPassword(event: Event): void {
    event.preventDefault();
    const email = this.loginForm.get('email')?.value;

    if (!email) {
      alert('Por favor, introduce tu correo electrónico antes de continuar.');
      return;
    }

    alert(`Se ha enviado a su correo (${email}) el proceso para restablecer su contraseña.`);
  }

  onSubmit() {

    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = null;

    this.authService.login(this.loginForm.value).subscribe({
      next: (response) => {
        const authResponse =
          typeof response === 'string' ? JSON.parse(response) : response;

        this.authService.saveToken(authResponse.token);
        this.authService.saveRole(authResponse.tipoRol);
        const rol = authResponse.tipoRol;

        switch (rol) {
          case 'USUARIO':
            this.router.navigate(['/usuario/']);
            break;
          case 'MONITOR':
            this.router.navigate(['/monitor/']);
            break;
          case 'ADMON':
            this.router.navigate(['/admin/']);
            break;
          default:
            this.router.navigate(['']);
            break;
        }
      },
      error: err => {
        if (err.status === 401) {
          this.error = 'Credenciales incorrectas.';
        } else {
          this.error = 'No se pudo iniciar sesión. Inténtalo más tarde.';
        }
        this.loading = false;
      }
    });
  }
}
