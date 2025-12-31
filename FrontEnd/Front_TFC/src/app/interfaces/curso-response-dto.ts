import { PlanificacionCursoResponseDto } from './planificacion-curso-response-dto';

export interface CursoResponseDto {
    idCurso: number;
    nombre: string;
    descripcion: string;
    fechaInicio: string;        
    fechaFin: string;  
    estado: string;   
    aforoMaximo: number;
    minimoAsistencia: number;
    precio: number;
    planificaciones: PlanificacionCursoResponseDto[];
    plazasLibres: number;
    zona: string;      
    nombreMonitor: string;
}
