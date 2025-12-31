export interface LoginResponseDto {
    email: string,
    // password: string,
    token: string,
    // rol: string,
    enabled: number;
    tipoRol: string;   
    nombre: string;
}
