import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { UsuarioResponseDto } from '../../../interfaces/usuario-response-dto';
import { AdminService } from '../../../services/admin.service';
import { UsuarioService } from '../../../services/usuario.service';

@Component({
  selector: 'app-admin-monitores',
  standalone: true,
  imports: [],
  templateUrl: './admin-monitores.component.html',
  styleUrl: './admin-monitores.component.css'
})
export class AdminMonitoresComponent {
  monitores: UsuarioResponseDto[] = [];

  private adminService = inject(AdminService);
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);

  ngOnInit(): void {
    this.usuarioService.getTodos().subscribe({
      next: (data) => {
        this.monitores = data.filter(u => u.rol === 'MONITOR');
      },
      error: (err) => console.error('Error al cargar monitores:', err),
    });
  }

  deshabilitar(email: string) {
    const confirmar = confirm(`¿Deshabilitar al monitor ${email}?`);
    if (!confirmar) return;

    this.adminService.deshabilitarUsuario(email).subscribe({
      next: () => {
        this.monitores = this.monitores.map(u =>
          u.email === email ? { ...u, enabled: 0 } : u
        );
        alert('Monitor deshabilitado correctamente');
      },
      error: (err) => {
        console.error('Error al deshabilitar monitor:', err);
        alert('No se pudo deshabilitar el monitor');
      }
    });
  }

  habilitar(email: string) {
    const confirmar = confirm(`¿Habilitar al monitor ${email}?`);
    if (!confirmar) return;

    this.adminService.habilitarUsuario(email).subscribe({
      next: (res) => {
        alert(res.message || 'Monitor habilitado');
        this.usuarioService.getTodos().subscribe(data => {
          this.monitores = data.filter(u => u.rol === 'MONITOR');
        });
      },
      error: (err) => {
        console.error('Error al habilitar monitor:', err);
        alert(err.error?.message || 'Error al habilitar monitor');
      }
    });
  }

  agregarMonitor() {
    this.router.navigate(['/admin/monitores/alta']);
  }

  volverAlPanel() {
    this.router.navigate(['/admin']);
  }
}
