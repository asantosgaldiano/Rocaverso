import { Component } from '@angular/core';
import { JumbotronComponent } from "../../../components/public/jumbotron/jumbotron.component";
import { AforoCardComponent } from "../../../components/public/aforo-card/aforo-card.component";
import { EventosListComponent } from "../eventos-list/eventos-list.component";
import { CursosListComponent } from "../cursos-list/cursos-list.component";

@Component({
  selector: 'app-home-public',
  standalone: true,
  imports: [JumbotronComponent, AforoCardComponent, EventosListComponent, CursosListComponent],
  templateUrl: './home-public.component.html',
  styleUrl: './home-public.component.css'
})
export class HomePublicComponent {

}
