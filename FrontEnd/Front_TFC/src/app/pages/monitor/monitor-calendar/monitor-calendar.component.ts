import { Component, OnInit, signal, ViewChild } from '@angular/core';
import { FullCalendarComponent, FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions, EventInput } from '@fullcalendar/core/index.js';
import { addDays } from 'date-fns';
import { lastValueFrom } from 'rxjs';
import { CursoService } from '../../../services/curso.service';
import { CommonModule } from '@angular/common';
import esLocale from '@fullcalendar/core/locales/es';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import { Router } from '@angular/router';

@Component({
  selector: 'app-monitor-calendar',
  standalone: true,
  imports: [CommonModule, FullCalendarModule],
  templateUrl: './monitor-calendar.component.html',
  styleUrl: './monitor-calendar.component.css'
})
export class MonitorCalendarComponent implements OnInit {

  // COMENTARIOS EXPLICATIVOS DEL CÓDIGO EN USUARIO-CALENDAR,
  // APLICA LA MISMA LÓGICA

  calendarOptions = signal<CalendarOptions | undefined>(undefined);
  @ViewChild('fullCalendar') calendarComponent?: FullCalendarComponent;

  constructor(
    private cursoService: CursoService,
      private router: Router) {}

  cambiarVista(vista: 'dayGridMonth' | 'timeGridWeek' | 'timeGridDay') {
    const calendarApi = this.calendarComponent?.getApi();
    if (calendarApi) {
      calendarApi.changeView(vista);
    }
  }

  async ngOnInit(): Promise<void> {
    try {
      const cursos = await lastValueFrom(this.cursoService.getCursosDelMonitor());
      const eventos: EventInput[] = [];

      // PERSONALIZAR AL GUSTO
      const coloresCursos = ['#007B8A', '#00A8A8', '#339999', '#66B2B2', '#33CCCC', '#00CCCC', '#009999'];
      
      
      cursos.forEach((curso, index) => {
         const fechaInicio = this.parseDateWithoutTimezone(curso.fechaInicio);
        const fechaFin = this.parseDateWithoutTimezone(curso.fechaFin);

        const color = coloresCursos[index % coloresCursos.length];

        curso.planificaciones.forEach(plan => {
          const diaSemana = this.getDiaSemanaIndex(plan.diaSemana);

          let fechaClase = new Date(fechaInicio);
          const diaActual = fechaClase.getDay();

          let offset = diaSemana - diaActual;
          if (offset < 0) offset += 7;

          fechaClase = addDays(fechaClase, offset);

          while (fechaClase <= fechaFin) {
            const fechaStr = fechaClase.toISOString().split('T')[0];

            eventos.push({
              title: curso.nombre,
              start: `${fechaStr}T${plan.horaInicio}`,
              end: `${fechaStr}T${plan.horaFin}`,
              color,
              extendedProps: {
                horaInicio: plan.horaInicio,
                horaFin: plan.horaFin
              }
            });

            fechaClase = addDays(fechaClase, 7);
          }
        });
      });

      this.calendarOptions.set({
        initialView: 'dayGridMonth',
        plugins: [dayGridPlugin, timeGridPlugin],
        events: eventos,
        locale: esLocale,
        firstDay: 1,
        eventDisplay: 'block',
        eventContent: (arg) => {
          const inicio = arg.event.extendedProps['horaInicio'];
          const fin = arg.event.extendedProps['horaFin'];

          return {
            html: `
              <div style="white-space: normal; font-size:0.75rem;">
                <strong>${arg.event.title}</strong><br>
                ${inicio} - ${fin}
              </div>
            `
          };
        }
      });

    } catch (error) {
      console.error('Error al cargar el calendario del monitor:', error);
    }
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
      default: throw new Error(`Día inválido: ${dia}`);
    }
  }

  private parseDateWithoutTimezone(dateStr: string): Date {
    const [year, month, day] = dateStr.split('-').map(Number);
    return new Date(year, month - 1, day, 12, 0, 0);
  }

  volverAlPanel() {
    this.router.navigate(['/monitor']);
  }

}
