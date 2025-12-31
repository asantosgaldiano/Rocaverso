import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { UsuarioResponseDto } from '../../../interfaces/usuario-response-dto';
import { AdminService } from '../../../services/admin.service';
import { UsuarioService } from '../../../services/usuario.service';

@Component({
  selector: 'app-admin-administradores',
  standalone: true,
  imports: [],
  templateUrl: './admin-administradores.component.html',
  styleUrl: './admin-administradores.component.css'
})

export class AdminAdministradoresComponent {

  // Lista local con los administradores
  administradores: UsuarioResponseDto[] = [];

  // Inyectamos servicios necesarios
  private adminService = inject(AdminService);
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);

  ngOnInit(): void {
    // Cargo todos los usuarios y filtro solo los administradores
    this.usuarioService.getTodos().subscribe({
      next: (data) => {
        this.administradores = data.filter(u => u.rol === 'ADMON');
      },
      error: (err) => console.error('Error al cargar administradores:', err),
    });
  }

  deshabilitar(email: string) {
    // Confirmación antes de deshabilitar
    const confirmar = confirm(`¿Deshabilitar al administrador ${email}?`);
    if (!confirmar) return;

    // Llamada al backend para deshabilitar
    this.adminService.deshabilitarUsuario(email).subscribe({
      next: () => {
        // Actualizo la lista en memoria cambiando el campo enabled
        this.administradores = this.administradores.map(u =>
          u.email === email ? { ...u, enabled: 0 } : u
        );
        alert('Administrador deshabilitado correctamente');
      },
      error: (err) => {
        console.error('Error al deshabilitar administrador:', err);
        alert('No se pudo deshabilitar el administrador');
      }
    });
  }

  habilitar(email: string) {
    // Confirmación antes de habilitar
    const confirmar = confirm(`¿Habilitar al administrador ${email}?`);
    if (!confirmar) return;

    // Llamada al backend para habilitar
    this.adminService.habilitarUsuario(email).subscribe({
      next: (res) => {
        alert(res.message || 'Administrador habilitado');

        // Recargo los usuarios para actualizar la lista
        this.usuarioService.getTodos().subscribe(data => {
          this.administradores = data.filter(u => u.rol === 'ADMON');
        });
      },
      error: (err) => {
        console.error('Error al habilitar administrador:', err);
        alert(err.error?.message || 'Error al habilitar administrador');
      }
    });
  }

  agregarAdmin() {
    // Navego al formulario de alta
    this.router.navigate(['/admin/administradores/alta']);
  }

  volverAlPanel() {
    // Vuelvo al dashboard del administrador
    this.router.navigate(['/admin']);
  }
}

