import { Component } from '@angular/core';
import { NavbarComponent } from "../../../components/public/navbar/navbar.component";
import { RouterOutlet } from "@angular/router";
import { FooterComponent } from "../../../components/public/footer/footer.component";

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [NavbarComponent, RouterOutlet, FooterComponent],
  templateUrl: './admin-layout.component.html',
  styleUrl: './admin-layout.component.css'
})
export class AdminLayoutComponent {

}
