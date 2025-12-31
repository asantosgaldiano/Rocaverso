import { Component } from '@angular/core';
import { EventosListComponent } from "../eventos-list/eventos-list.component";
import { CursosListComponent } from "../cursos-list/cursos-list.component";

@Component({
  selector: 'app-actividades-detail',
  standalone: true,
  imports: [EventosListComponent, CursosListComponent],
  templateUrl: './actividades-detail.component.html',
  styleUrl: './actividades-detail.component.css'
})
export class ActividadesDetailComponent {

}
