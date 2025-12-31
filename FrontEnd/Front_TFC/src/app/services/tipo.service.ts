import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../enviroments/enviroment';
import { Tipo } from '../interfaces/tipo';

@Injectable({
  providedIn: 'root'
})

export class TipoService {

  private apiUrl = `${environment.apiUrl}/tipo`;
  private http = inject(HttpClient);

  constructor() { }

  getTodos(): Observable<Tipo[]> {
    return this.http.get<Tipo[]>(`${this.apiUrl}/all`);
  }  
  
}
