import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class cargoAdministrativoService{
  private readonly API_URL = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  registrarCargo(nombre: string): Observable<any> {
  return this.http.post(`${this.API_URL}/cargo-administrativos`, { nombreCargo: nombre });
}
}