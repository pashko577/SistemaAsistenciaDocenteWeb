import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Token } from './token';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private API_URL = 'http://localhost:8080/api/auth';

  constructor(
    private http: HttpClient,
    private tokenService: Token,
    private router: Router
  ){}

  login(data: {username: string; password: string}): Observable<any> {
    return this.http.post(`${this.API_URL}/login`, data);
  }

  register(data: any): Observable<void>{
    return this.http.post<void>(`${this.API_URL}/register`, data);
  }

  logout(): void{
    this.tokenService.removeToken();
    this.router.navigate(['/login']);
  }

}
