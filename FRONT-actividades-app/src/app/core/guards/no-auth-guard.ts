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

  canActivate(): boolean  {
    if (this.tokenService.isLogged()){

      const roles = this.tokenService.getRoles();
      // Si YA está logueado → redirigir

      if (roles.includes('ADMIN')) {
        this.router.navigate(['/admin']);
      } else if (roles.includes('USER')) {
        this.router.navigate(['/user']);        
      } else {
        this.router.navigate(['/'])
      }
      return false;
    }

    // No está logueado → puede entrar a login/register
    return true;    
  }
}
