import { PlanificacionCursoRequestDto } from "./planificacion-curso-request-dto";

export interface CursoRequestDto {
    nombre: string;
    descripcion: string;
    fechaInicio: string;
    fechaFin: string;
    aforoMaximo: number;
    minimoAsistencia: number;
    precio: number;
    zona: string;
    idRocodromo: number;
    emailMonitor: string;
    planificaciones: PlanificacionCursoRequestDto[];
}
