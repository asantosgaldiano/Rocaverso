import { Component, HostListener, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})

export class NavbarComponent implements OnInit {

  // Servicio de autenticación para saber si el usuario ha iniciado sesión
  auth: AuthService = inject(AuthService);

  // Controla si el menú móvil está desplegado
  menuAbierto = false;

  // Indica si la pantalla es pequeña (móvil/tablet)
  screenIsSmall = window.innerWidth < 768;

  ngOnInit(): void {
    // Se calcula el estado inicial del tamaño de pantalla
    this.onResize(); 
  }

  // Escucha cambios en el tamaño de la ventana
  @HostListener('window:resize', [])
  onResize() {
    // Detecta si la pantalla es menor que 768px
    this.screenIsSmall = window.innerWidth < 768;

    // Si dejo de estar en móvil, cierro el menú automáticamente
    if (!this.screenIsSmall) {
      this.menuAbierto = false;
    }
  }

  toggleMenu(): void {
    // Abre o cierra el menú móvil
    this.menuAbierto = !this.menuAbierto;
  }

  logout(): void {
    // Llama al servicio para cerrar sesión y limpiar token
    this.auth.logout();
  }
}
