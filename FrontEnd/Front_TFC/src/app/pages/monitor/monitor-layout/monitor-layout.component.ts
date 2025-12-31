import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FooterComponent } from '../../../components/public/footer/footer.component';
import { NavbarComponent } from '../../../components/public/navbar/navbar.component';

@Component({
  selector: 'app-monitor-layout',
  standalone: true,
  imports: [NavbarComponent, FooterComponent, RouterModule],
  templateUrl: './monitor-layout.component.html',
  styleUrl: './monitor-layout.component.css'
})

export class MonitorLayoutComponent {

}
