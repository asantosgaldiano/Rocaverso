import { Routes } from '@angular/router';
import { PublicLayoutComponent } from './pages/public/public-layout/public-layout.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { usuarioGuard } from './guards/usuario.guard';
import { UsuarioLayoutComponent } from './pages/usuario/usuario-layout/usuario-layout.component';
import { monitorGuard } from './guards/monitor.guard';
import { MonitorLayoutComponent } from './pages/monitor/monitor-layout/monitor-layout.component';
import { adminGuard } from './guards/admin.guard';
import { AdminLayoutComponent } from './pages/admin/admin-layout/admin-layout.component';

export const routes: Routes = [

  // Rutas sin layout (pantallas independientes)
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // Ruta con parámetro dinámico para ver un evento público
  { 
    path: 'evento/:idEvento', 
    // Carga el componente solo cuando se necesita (lazy loading)
    loadComponent: () => import('./pages/public/evento-detail/evento-detail.component')
      .then(m => m.EventoDetailComponent) 
  },

  // Ruta para detalle de curso público (id recibido por URL)
  { 
    path: 'curso/:idCurso', 
    loadComponent: () => import('./pages/public/curso-detail/curso-detail.component')
      .then(m => m.CursoDetailComponent) 
  },

  // Rutas públicas que usan layout general del sitio
  {
    path: '',
    component: PublicLayoutComponent, // Layout base para visitantes
    children: [

        // Página principal pública
        {
          path: '',
          loadComponent: () =>
            import('./pages/public/home-public/home-public.component')
              .then(m => m.HomePublicComponent)
        },

        // Página de información del rocódromo
        {
          path: 'rocodromo',
          loadComponent: () =>import('./pages/public/rocodromo-detail/rocodromo-detail.component')
            .then((m) => m.RocodromoDetailComponent),
        },

        // Página para ver las actividades
        {
          path: 'actividades',
          loadComponent: () =>import('./pages/public/actividades-detail/actividades-detail.component')
            .then((m) => m.ActividadesDetailComponent),
        },
    ]
  },

  // USUARIO (zona privada)
  {
    path: 'usuario',
    // Solo usuarios autenticados pueden entrar
    canActivate: [usuarioGuard],
    component: UsuarioLayoutComponent, // Layout para usuarios normales
    children: [
        {
          path: '',
          loadComponent: () =>import('./pages/usuario/usuario-dashboard/usuario-dashboard.component')
            .then((m) => m.UsuarioDashboardComponent),
        },
        {
          path: 'vias',
          loadComponent: () =>import('./pages/usuario/vias-list/vias-list.component')
            .then((m) => m.ViasListComponent),
        },
        {
          path: 'calendario',
          loadComponent: () =>import('./pages/usuario/usuario-calendar/usuario-calendar.component')
            .then((m) => m.UsuarioCalendarComponent),
        },
        {
          path: 'editar',
          loadComponent: () =>import('./pages/usuario/usuario-editar-perfil/usuario-editar-perfil.component')
            .then((m) => m.UsuarioEditarPerfilComponent),
        },
      ],
  },

  // MONITOR (zona privada)
  {
    path: 'monitor',
    canActivate: [monitorGuard], // solo rol monitor
    component: MonitorLayoutComponent,
    children: [
        {
          path: '',
          loadComponent: () =>import('./pages/monitor/monitor-dashboard/monitor-dashboard.component')
            .then((m) => m.MonitorDashboardComponent),
        },
        {
          path: 'calendario',
          loadComponent: () =>import('./pages/monitor/monitor-calendar/monitor-calendar.component')
            .then((m) => m.MonitorCalendarComponent),
        },
        {
          // detalle del curso asignado al monitor
          path: 'curso/:idCurso',
          loadComponent: () =>import('./pages/monitor/monitor-curso-detail/monitor-curso-detail.component')
            .then((m) => m.MonitorCursoDetailComponent),
        },
        {
          // Monitores también pueden editar su perfil (reutiliza componente de usuario)
          path: 'editar',
          loadComponent: () =>import('./pages/usuario/usuario-editar-perfil/usuario-editar-perfil.component')
            .then((m) => m.UsuarioEditarPerfilComponent),
        },
      ],
  },

  // ADMIN (zona privada)
  {
    path: 'admin',
    canActivate: [adminGuard], // solo administradores
    component: AdminLayoutComponent,
    children: [
        {
          path: '',
          loadComponent: () =>import('./pages/admin/admin-dashboard/admin-dashboard.component')
            .then((m) => m.AdminDashboardComponent),
        },

        // Editar perfil (usa mismo componente que usuario y monitor)
        {
          path: 'editar',
          loadComponent: () =>import('./pages/usuario/usuario-editar-perfil/usuario-editar-perfil.component')
            .then((m) => m.UsuarioEditarPerfilComponent),
        },

        // Gestión de usuarios
        {
          path: 'usuarios',
          loadComponent: () =>import('./pages/admin/admin-users/admin-users.component')
            .then((m) => m.AdminUsersComponent),
        },

        // Gestión de eventos
        {
          path: 'eventos',
          loadComponent: () =>import('./pages/admin/admin-eventos/admin-eventos.component')
            .then((m) => m.AdminEventosComponent),
        },
        {
          path: 'eventos/editar/:idEvento',
          loadComponent: () =>import('./pages/admin/admin-eventos/evento-edit/evento-edit.component')
            .then((m) => m.EventoEditComponent),
        },
        {
          path: 'eventos/alta',
          loadComponent: () =>import('./pages/admin/admin-eventos/evento-add/evento-add.component')
            .then((m) => m.EventoAddComponent),
        },

        // Gestión de vías
        {
          path: 'vias',
          loadComponent: () =>import('./pages/admin/admin-vias/admin-vias.component')
            .then((m) => m.AdminViasComponent),
        },
        {
          path: 'vias/editar/:idVia',
          loadComponent: () =>import('./pages/admin/admin-vias/via-edit/via-edit.component')
            .then((m) => m.ViaEditComponent),
        },
        {
          path: 'vias/alta',
          loadComponent: () =>import('./pages/admin/admin-vias/via-add/via-add.component')
            .then((m) => m.ViaAddComponent),
        },

        // Gestión de monitores
        {
          path: 'monitores',
          loadComponent: () =>import('./pages/admin/admin-monitores/admin-monitores.component')
            .then((m) => m.AdminMonitoresComponent),
        },
        {
          path: 'monitores/alta',
          loadComponent: () =>import('./pages/admin/admin-monitores/monitor-add/monitor-add.component')
            .then((m) => m.MonitorAddComponent),
        },

        // Gestión de administradores
        {
          path: 'administradores',
          loadComponent: () =>import('./pages/admin/admin-administradores/admin-administradores.component')
            .then((m) => m.AdminAdministradoresComponent),
        },
        {
          path: 'administradores/alta',
          loadComponent: () =>import('./pages/admin/admin-administradores/admin-add/admin-add.component')
            .then((m) => m.AdminAddComponent),
        },

        // Gestión de cursos
        {
          path: 'cursos',
          loadComponent: () =>import('./pages/admin/admin-cursos/admin-cursos.component')
            .then((m) => m.AdminCursosComponent),
        },
        {
          path: 'cursos/editar/:idCurso',
          loadComponent: () =>import('./pages/admin/admin-cursos/curso-edit/curso-edit.component')
            .then((m) => m.CursoEditComponent),
        },
        {
          path: 'cursos/alta',
          loadComponent: () =>import('./pages/admin/admin-cursos/curso-add/curso-add.component')
            .then((m) => m.CursoAddComponent),
        },

        // Calendario completo del rocódromo
        {
          path: 'calendario',
          loadComponent: () =>import('./pages/admin/admin-calendar/admin-calendar.component')
            .then((m) => m.AdminCalendarComponent),
        },
      ],
  },

    // IMPORTANTE sea la ULTIMA.
    // si la ruta no existe, al componente de rutaserroneas,
    //{ path: '**', component: Page404Component}

    // si la ruta no existe, otra opcion,
    {path: '**', redirectTo:''}

];


