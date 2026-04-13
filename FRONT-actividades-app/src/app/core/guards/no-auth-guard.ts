import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Token } from '../services/token';

@Injectable({
  providedIn: 'root',
})

export class NoAuthGuard implements CanActivate {

  constructor(
    private tokenService: Token,
    private router: Router
  ){}


canActivate(): boolean {
  if (this.tokenService.isLogged()) {
    // Si ya está logueado, lo mandamos al dashboard global.
    // El AuthGuard se encargará de validar si tiene permiso allí.
    this.router.navigate(['/dashboard']); 
    return false;
  }
  return true;
}
}
