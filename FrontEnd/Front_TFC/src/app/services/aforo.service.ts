import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../enviroments/enviroment';
import { AforoActualDto } from '../interfaces/aforo-actual-dto';

@Injectable({
  providedIn: 'root'
})
export class AforoService {
  
  private apiUrl = `${environment.apiUrl}/aforo`;
  private http: HttpClient = inject(HttpClient);

  constructor() { }

  getAforoActual(): Observable<AforoActualDto> {
    return this.http.get<AforoActualDto>(`${this.apiUrl}/actual`);
  }
}
