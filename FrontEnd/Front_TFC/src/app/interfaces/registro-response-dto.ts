import { UsuarioResponseDto } from "./usuario-response-dto";

export interface RegistroResponseDto {
    usuario: UsuarioResponseDto;
    token: string;
}
