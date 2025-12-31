import { Component, HostListener } from '@angular/core';
import { NavbarComponent } from "../../../components/public/navbar/navbar.component";
import { FooterComponent } from "../../../components/public/footer/footer.component";
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-usuario-layout',
  standalone: true,
  imports: [NavbarComponent, FooterComponent, RouterModule],
  templateUrl: './usuario-layout.component.html',
  styleUrl: './usuario-layout.component.css'
})
export class UsuarioLayoutComponent {

}
