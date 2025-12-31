import { Component, OnInit, signal, ViewChild } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { ViaRealizadaService } from '../../../services/via-realizada.service';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import { FullCalendarComponent, FullCalendarModule } from '@fullcalendar/angular';
import { CommonModule } from '@angular/common';
import { lastValueFrom } from 'rxjs';
import esLocale from '@fullcalendar/core/locales/es';
import { parseISO, addDays } from 'date-fns';
import { Router } from '@angular/router';

@Component({
  selector: 'app-usuario-calendar',
  standalone: true,
  imports: [CommonModule, FullCalendarModule],
  templateUrl: './usuario-calendar.component.html',
  styleUrl: './usuario-calendar.component.css'
})
export class UsuarioCalendarComponent implements OnInit {

  // Aquí guardo la configuración del calendario usando una señal
  calendarOptions = signal<CalendarOptions | undefined>(undefined);

  // Guardo una referencia al componente del calendario para poder cambiar la vista
  @ViewChild('fullCalendar') calendarComponent?: FullCalendarComponent;

  constructor(
    private usuarioService: UsuarioService,
    private viaRealizadaService: ViaRealizadaService,
    private router: Router
  ) {}

  // Con este método cambio la vista del calendario (mes, semana o día)
  cambiarVista(vista: 'dayGridMonth' | 'timeGridWeek' | 'timeGridDay') {
    this.calendarComponent?.getApi().changeView(vista);
  }

  async ngOnInit(): Promise<void> {
    try {

      // Cargo mis inscripciones, reservas y vías realizadas desde el backend
      const inscripciones = await lastValueFrom(this.usuarioService.obtenerMisInscripcionesCalendar());
      const reservas = await lastValueFrom(this.usuarioService.obtenerMisReservas());
      const vias = await lastValueFrom(this.viaRealizadaService.getMisVias());

      const eventos: EventInput[] = [];

      /* PALETA DE COLORES */
      const coloresInscripciones = ['#007B8A', '#009688', '#00A8A8', '#33CCCC', '#66B2B2', '#339999'];
      const coloresReservas = ['#c05621', '#dd6b20', '#d97706', '#ed8936', '#f6ad55'];
      const coloresVias = ['#3A7753', '#4E8A5E', '#6AA978', '#82BF8E', '#97D3A3'];

      let insIndex = 0;
      let resIndex = 0;
      let viaIndex = 0;

      // INSCRIPCIONES
      inscripciones.forEach(inscripcion => {
        // Obtengo un color de la lista. Uso el operador % para que,
        // si llego al final del array, vuelva a empezar desde el principio.
        const color = coloresInscripciones[insIndex % coloresInscripciones.length];
        // Aumento el índice para que la siguiente vía use otro color
        insIndex++;

        // Convierto las fechas del curso a objetos Date
        const fechaInicioCurso = this.parseDate(inscripcion.fechaInicioCurso);
        const fechaFinCurso = this.parseDate(inscripcion.fechaFinCurso);

        // Cada planificación indica el día de la semana y horario
        inscripcion.planificaciones.forEach(plan => {

          
          const diaSemanaIndex = this.getDiaSemanaIndex(plan.diaSemana);
          let fechaActual = new Date(fechaInicioCurso);
          
          // Calculo cuándo cae la primera clase según el día de la semana
          let diff = diaSemanaIndex - fechaActual.getDay();
          if (diff < 0) diff += 7;
          // Si la fecha de inicio es ya el día correcto, diasParaPrimeraClase será 0, sin sumar días.

          fechaActual = addDays(fechaActual, diff);
          
          // Voy generando un evento semanal hasta la fecha fin
          while (fechaActual <= fechaFinCurso) {
            const fechaStr = fechaActual.toISOString().split('T')[0];

            eventos.push({
              // Nombre del curso que aparecerá en el calendario
              title: inscripcion.nombreCurso,
              // Construyo la fecha completa combinando el día calculado + la hora del plan
              start: `${fechaStr}T${plan.horaInicio}`,
              end: `${fechaStr}T${plan.horaFin}`,

              // Color asignado a esta inscripción (según la paleta rotatoria)
              color: color,

              // Información extra que FullCalendar no muestra, pero yo puedo usar luego
              extendedProps: {
                
              // Indico que este evento es una inscripción (me sirve para personalizar su renderizado)
                tipo: 'inscripcion',
                horaInicio: plan.horaInicio,
                horaFin: plan.horaFin
              }
            });

            // Avanzar 7 días para la siguiente repetición
            fechaActual = addDays(fechaActual, 7);
          }
        });
      });

      // RESERVAS
      reservas.forEach(r => {
        const color = coloresReservas[resIndex % coloresReservas.length];
        resIndex++;

        // Cada reserva es un solo evento en el calendario
        eventos.push({
          title: r.nombreEvento,
          start: `${r.fechaInicioEvento}T${r.horaInicio}`,
          end: `${r.fechaInicioEvento}T${r.horaFin}`,
          color,
          extendedProps: {
            tipo: 'reserva',
            horaInicio: r.horaInicio,
            horaFin: r.horaFin
          }
        });
      });

      // VÍAS REALIZADAS
      vias.forEach(v => {
        const color = coloresVias[viaIndex % coloresVias.length];
        viaIndex++;

        // Para las vías realizadas solo muestro una línea simple        
        eventos.push({
          title: `Vía: ${v.tipo} (${v.dificultad})`,
          start: v.fechaRealizacion,
          color: color,
          extendedProps: {
            tipo: 'via'
          }
        });
      });

      // Aquí configuro el calendario con todos los eventos que generé
      this.calendarOptions.set({
        initialView: 'dayGridMonth',
        plugins: [dayGridPlugin, timeGridPlugin],
        locale: esLocale,
        firstDay: 1,
        events: eventos,
        eventDisplay: 'block',

        // Personalizo cómo se muestran los eventos dentro del calendario
        eventContent: (arg) => {
          const tipo = arg.event.extendedProps['tipo'];

          // Cursos y reservas - 2 líneas
          // Si es curso o reserva, muestro el título y las horas          
          if (tipo === 'inscripcion' || tipo === 'reserva') {
            const inicio = arg.event.extendedProps['horaInicio'];
            const fin = arg.event.extendedProps['horaFin'];
            return {
              html: `
                <div style="white-space: normal; font-size: 0.75rem;">
                  <strong>${arg.event.title}</strong><br>
                  ${inicio} - ${fin}
                </div>
              `
            };
          }

          // Vías realizadas - 1 línea simple
          // Si es vía realizada, muestro solo el título
          return {
            html: `
              <div style="white-space: normal; font-size: 0.75rem;">
                ${arg.event.title}
              </div>
            `
          };
        }
      });

    } catch (error) {
      console.error('Error cargando calendario:', error);
    }
  }

  // Convierto una fecha tipo "2025-03-10" en un Date
  private parseDate(dateStr: string): Date {
    const [y, m, d] = dateStr.split('-').map(Number);
    return new Date(y, m - 1, d, 12, 0, 0); // uso hora media para evitar problemas de zona horaria
  }

  // Paso un día de la semana en texto a un número (lunes=1,...)
  private getDiaSemanaIndex(dia: string): number {
    switch (dia.toUpperCase()) {
      case 'LUNES': return 1;
      case 'MARTES': return 2;
      case 'MIERCOLES': return 3;
      case 'JUEVES': return 4;
      case 'VIERNES': return 5;
      case 'SABADO': return 6;
      case 'DOMINGO': return 0;
      default: throw new Error(`Día de la semana no válido: ${dia}`);
    }
  }

  volverAlPanel() {
    this.router.navigate(['/usuario']);
  }
}

