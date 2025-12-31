import { PlanificacionCursoResponseDto } from './planificacion-curso-response-dto';

export interface MisInscripcionesCalendarDto {
    nombreCurso: string;
    fechaInicioCurso: string;
    fechaFinCurso: string;
    planificaciones: PlanificacionCursoResponseDto[];
}
