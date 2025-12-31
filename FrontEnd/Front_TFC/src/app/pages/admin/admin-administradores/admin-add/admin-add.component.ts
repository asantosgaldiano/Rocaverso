import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { UsuarioRequestDto } from '../../../../interfaces/usuario-request-dto';
import { AdminService } from '../../../../services/admin.service';

@Component({
  selector: 'app-admin-add',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './admin-add.component.html',
  styleUrl: './admin-add.component.css'
})

export class AdminAddComponent {
  form!: FormGroup;
  error: string | null = null;
  loading = false;
  mostrarPassword = false;

  // Inyectamos los servicios necesarios
  private fb = inject(FormBuilder);
  private adminService = inject(AdminService);
  private router = inject(Router);

  ngOnInit() {
    // Construcción del formulario con validaciones
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      repetirPassword: ['', Validators.required]
    }, { validators: this.passwordsIgualesValidator }); // Validador para comparar contraseñas
  }

  // Valida que password y repetirPassword sean iguales
  passwordsIgualesValidator(group: AbstractControl): ValidationErrors | null {
    const pass = group.get('password')?.value;
    const repetir = group.get('repetirPassword')?.value;

    // Si son iguales, válido (null). Si no, error personalizado.
    return pass === repetir ? null : { passwordMismatch: true };
  }

  // Alterna entre mostrar/ocultar la contraseña
  toggleMostrarPassword(): void {
    this.mostrarPassword = !this.mostrarPassword;
  }

  onSubmit(): void {
    // Si el formulario es inválido, marco todos los campos y detengo el envío
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = null;

    // Preparo el objeto con los datos del admin nuevo
    const usuario: UsuarioRequestDto = {
      email: this.form.value.email,
      nombre: this.form.value.nombre,
      apellidos: this.form.value.apellidos,
      password: this.form.value.password
    };

    // Llamo al backend para registrar al administrador
    this.adminService.altaAdmin(usuario).subscribe({
      next: () => {
        alert('Administrador registrado correctamente');
        this.router.navigate(['/admin/administradores']); // Redirección al listado
      },
      error: (err) => {
        this.error = 'Error al registrar el administrador. Inténtalo de nuevo.';
        console.error(err);
        this.loading = false;
      },
      complete: () => {
        this.loading = false; // Finaliza el estado de carga
      }
    });
  }
}

