import { Component, inject } from '@angular/core';
import { AdminService } from '../../../../services/admin.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { UsuarioRequestDto } from '../../../../interfaces/usuario-request-dto';

@Component({
  selector: 'app-monitor-add',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './monitor-add.component.html',
  styleUrl: './monitor-add.component.css'
})

export class MonitorAddComponent {
  form!: FormGroup;
  error: string | null = null;
  loading = false;
  mostrarPassword = false;

  private fb = inject(FormBuilder);
  private adminService = inject(AdminService);
  private router = inject(Router);

  ngOnInit() {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      repetirPassword: ['', Validators.required]
    }, { validators: this.passwordsIgualesValidator });
  }

  passwordsIgualesValidator(group: AbstractControl): ValidationErrors | null {
    const pass = group.get('password')?.value;
    const repetir = group.get('repetirPassword')?.value;
    return pass === repetir ? null : { passwordMismatch: true };
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

    const usuario: UsuarioRequestDto = {
      email: this.form.value.email,
      nombre: this.form.value.nombre,
      apellidos: this.form.value.apellidos,
      password: this.form.value.password
    };

    this.adminService.altaMonitor(usuario).subscribe({
      next: () => {
        alert('Monitor registrado correctamente');
        this.router.navigate(['/admin/monitores']);
      },
      error: (err) => {
        this.error = 'Error al registrar el monitor. Inténtalo de nuevo.';
        console.error(err);
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      }
    });
  }
}
