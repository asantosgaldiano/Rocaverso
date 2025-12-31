import { Component } from '@angular/core';
import { NavbarComponent } from "../../../components/public/navbar/navbar.component";
import { FooterComponent } from "../../../components/public/footer/footer.component";
import { RouterModule } from '@angular/router';
import { JumbotronComponent } from "../../../components/public/jumbotron/jumbotron.component";
import { EventosListComponent } from "../eventos-list/eventos-list.component";
import { CursosListComponent } from "../cursos-list/cursos-list.component";
import { AforoCardComponent } from "../../../components/public/aforo-card/aforo-card.component";

@Component({
  selector: 'app-public-layout',
  standalone: true,
  imports: [NavbarComponent,
    FooterComponent,
    RouterModule,],
  templateUrl: './public-layout.component.html',
  styleUrl: './public-layout.component.css'
})
export class PublicLayoutComponent {

}
