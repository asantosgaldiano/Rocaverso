import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { UsuarioRequestDto } from '../../../interfaces/usuario-request-dto';
import { UsuarioResponseDto } from '../../../interfaces/usuario-response-dto';
import { UsuarioService } from '../../../services/usuario.service';

@Component({
  selector: 'app-usuario-editar-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './usuario-editar-perfil.component.html',
  styleUrl: './usuario-editar-perfil.component.css'
})

export class UsuarioEditarPerfilComponent {
  private fb = inject(FormBuilder);
  private usuarioService = inject(UsuarioService);

  form!: FormGroup;
  loading = signal(true);
  error = signal<string | null>(null);
  private router = inject(Router);

  datosOriginales!: UsuarioResponseDto;
  mostrarPassword = false;

  ngOnInit(): void {
  this.usuarioService.obtenerMiPerfil().subscribe({
    next: (usuario) => {
      this.datosOriginales = usuario;
      this.form = this.fb.group(
        {
          nombre: this.fb.control(usuario.nombre, Validators.required),
          apellidos: this.fb.control(usuario.apellidos, Validators.required),
          password: this.fb.control(''),
          repetirPassword: this.fb.control('')
        },
        { validators: this.passwordsIgualesValidator }
      );
      this.loading.set(false);
    },
    error: () => {
      this.error.set('No se pudo cargar tu perfil');
      this.loading.set(false);
    }
  });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const formValue = this.form.value;

    const passwordFinal = formValue.password?.trim();
    const usuario: UsuarioRequestDto = {
      email: this.datosOriginales.email,
      nombre: formValue.nombre,
      apellidos: formValue.apellidos,
      password: passwordFinal !== '' ? passwordFinal : null
    };

    this.usuarioService.editarMiPerfil(usuario).subscribe({
      next: () => {
        alert('Perfil actualizado correctamente');
        this.router.navigate(['/usuario']); 
      },
      error: () => alert('Error al actualizar el perfil')
    });
  }
  passwordsIgualesValidator(group: AbstractControl): ValidationErrors | null {
    const pass = group.get('password')?.value;
    const repetir = group.get('repetirPassword')?.value;
    if (pass && repetir && pass !== repetir) {
      return { passwordMismatch: true };
    }
    return null;
  }

  toggleMostrarPassword(): void {
    this.mostrarPassword = !this.mostrarPassword;
  }
}