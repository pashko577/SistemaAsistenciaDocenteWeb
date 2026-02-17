import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Token } from '../services/token';

@Injectable({
  providedIn: 'root',
})
export class authGuard implements CanActivate{

  constructor(
    private tokenService: Token,
    private router: Router
  ){}

  canActivate(): boolean {

    if (this.tokenService.isLogged()){
      return true;
    }

    // No autenticado, redirigir al login
    this.router.navigate(['/login']);
    return false;
  }
}