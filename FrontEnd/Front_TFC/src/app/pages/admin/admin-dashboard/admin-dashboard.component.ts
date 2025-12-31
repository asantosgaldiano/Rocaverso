import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { UsuarioService } from '../../../services/usuario.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {

  nombreUsuario: string = '';  
  private usuarioService = inject(UsuarioService);

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.usuarioService.obtenerMiPerfil().subscribe({
      next: (data) => {
        this.nombreUsuario = data.nombre;
      },
      error: (error) => {
        console.error('Error al obtener perfil del usuario:', error);
      }
    });
  }

}
