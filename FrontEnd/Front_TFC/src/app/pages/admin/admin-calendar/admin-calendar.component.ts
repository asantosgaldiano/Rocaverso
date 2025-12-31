import { Component, OnInit, signal, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FullCalendarComponent, FullCalendarModule } from '@fullcalendar/angular';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import esLocale from '@fullcalendar/core/locales/es';
import { CalendarOptions, EventInput } from '@fullcalendar/core/index.js';
import { addDays } from 'date-fns';
import { lastValueFrom } from 'rxjs';
import { CursoService } from '../../../services/curso.service';
import { EventoService } from '../../../services/evento.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-calendar',
  standalone: true,
  imports: [CommonModule, FullCalendarModule],
  templateUrl: './admin-calendar.component.html',
  styleUrl: './admin-calendar.component.css'
})

export class AdminCalendarComponent implements OnInit {

  // COMENTARIOS EXPLICATIVOS DEL CÓDIGO EN USUARIO-CALENDAR,
  // APLICA LA MISMA LÓGICA

  calendarOptions = signal<CalendarOptions | undefined>(undefined);

  @ViewChild('fullCalendar') calendarComponent?: FullCalendarComponent;

  constructor(
    private cursoService: CursoService,
    private eventoService: EventoService,
    private router: Router
  ) {}

  cambiarVista(vista: 'dayGridMonth' | 'timeGridWeek' | 'timeGridDay') {
    this.calendarComponent?.getApi().changeView(vista);
  }

  async ngOnInit(): Promise<void> {
    try {
      const cursos = await lastValueFrom(this.cursoService.getActivos());
      const eventosActivos = await lastValueFrom(this.eventoService.getActivos());

      const eventos: EventInput[] = [];

          /* Paletas */
      const coloresCursos = [
        '#007B8A', '#009688', '#00A8A8', '#33CCCC',
        '#66B2B2', '#339999', '#00CCCC'
      ];

      const coloresEventos = [
        '#c05621', '#dd6b20', '#d97706',
        '#f6ad55', '#ed8936', '#c97a3d'
      ];

      /* CURSOS ACTIVOS */
      cursos.forEach((curso, indexCurso) => {

        const colorCurso = coloresCursos[indexCurso % coloresCursos.length];

        const fechaInicio = this.parseFecha(curso.fechaInicio);
        const fechaFin = this.parseFecha(curso.fechaFin);

        curso.planificaciones.forEach(plan => {

          const diaSemana = this.getDiaSemanaIndex(plan.diaSemana);

          let fechaActual = new Date(fechaInicio);
          const diaActual = fechaActual.getDay();

          let diferencia = diaSemana - diaActual;
          if (diferencia < 0) diferencia += 7;

          fechaActual = addDays(fechaActual, diferencia);

          while (fechaActual <= fechaFin) {
            const fechaStr = fechaActual.toISOString().split('T')[0];

            eventos.push({
              title: `${curso.nombre} (${plan.horaInicio} - ${plan.horaFin})`,
              start: `${fechaStr}T${plan.horaInicio}`,
              end: `${fechaStr}T${plan.horaFin}`,
              color: colorCurso
            });

            fechaActual = addDays(fechaActual, 7);
          }
        });
      });

      /* EVENTOS ACTIVOS */
      eventosActivos.forEach((ev, indexEvento) => {
        const colorEvento = coloresEventos[indexEvento % coloresEventos.length];

        eventos.push({
          title: `${ev.nombre} (${ev.horaInicio} - ${ev.horaFin})`,
          start: `${ev.fechaInicio}T${ev.horaInicio}`,
          end: `${ev.fechaInicio}T${ev.horaFin}`,
          color: colorEvento,
          extendedProps: {
            zona: ev.zona,
            precio: ev.precio
          }
        });
      });


      /* CONFIGURAR FULLCALENDAR */
      this.calendarOptions.set({
        initialView: 'dayGridMonth',
        plugins: [dayGridPlugin, timeGridPlugin],
        locale: esLocale,
        firstDay: 1,
        events: eventos,
        eventDisplay: 'block',
        eventContent: (arg) => {
          return {
            html: `
              <div style="white-space:normal; font-size:0.75rem;">
                ${arg.event.title}
              </div>
            `
          };
        }
      });

    } catch (error) {
      console.error('Error cargando calendario administrador:', error);
    }
  }

  private parseFecha(dateStr: string): Date {
    const [y, m, d] = dateStr.split('-').map(Number);
    return new Date(y, m - 1, d, 12, 0, 0);
  }

  private getDiaSemanaIndex(dia: string): number {
    switch (dia.toUpperCase()) {
      case 'LUNES': return 1;
      case 'MARTES': return 2;
      case 'MIERCOLES': return 3;
      case 'JUEVES': return 4;
      case 'VIERNES': return 5;
      case 'SABADO': return 6;
      case 'DOMINGO': return 0;
      default: return 1;
    }
  }

  volverAlPanel() {
    this.router.navigate(['/admin']);
  }
}
