export interface UsuarioResponseDto {
    email: string;
    nombre: string;
    apellidos: string;
    fechaRegistro: string;
    rol?: string;
    enabled?: number;
}
