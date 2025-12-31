import { Component, inject } from '@angular/core';
import { UsuarioResponseDto } from '../../../interfaces/usuario-response-dto';
import { AdminService } from '../../../services/admin.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-users',
  standalone: true,
  imports: [],
  templateUrl: './admin-users.component.html',
  styleUrl: './admin-users.component.css'
})

export class AdminUsersComponent {
  usuarios: UsuarioResponseDto[] = [];

  private adminService = inject(AdminService);
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);

  ngOnInit(): void {
    this.usuarioService.getTodos().subscribe({
      next: (data) => {
        this.usuarios = data.filter(u => u.rol === 'USUARIO' /*|| u.rol === 'MONITOR'*/);
      },
      error: (err) => console.error('Error al cargar usuarios:', err),
    });
  }

  deshabilitar(email: string) {
    const confirmar = confirm(`¿Deshabilitar al usuario ${email}?`);
    if (!confirmar) return;

    this.adminService.deshabilitarUsuario(email).subscribe({
      next: () => {
        this.usuarios = this.usuarios.map(u =>
          u.email === email ? { ...u, enabled: 0 } : u
        );
        alert('Usuario deshabilitado correctamente');
      },
      error: (err) => {
        console.error('Error al deshabilitar usuario:', err);
        alert('No se pudo deshabilitar el usuario');
      }
    });
  }

  habilitar(email: string) {
    const confirmar = confirm(`¿Habilitar al usuario ${email}?`);
    if (!confirmar) return;

    this.adminService.habilitarUsuario(email).subscribe({
      next: (res) => {
        alert(res.message || 'Usuario habilitado');
        this.usuarioService.getTodos().subscribe(data => {
          this.usuarios = data.filter(u => u.rol === 'USUARIO' /*|| u.rol === 'MONITOR'*/);
        });
      },
      error: (err) => {
        console.error('Error al habilitar:', err);
        alert(err.error?.message || 'Error al habilitar usuario');
      }
    });
  }

  volverAlPanel() {
    this.router.navigate(['/admin']);
  }
}
