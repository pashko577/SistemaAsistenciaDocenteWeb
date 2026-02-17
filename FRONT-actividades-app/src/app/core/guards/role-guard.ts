import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Route, Router, RouterStateSnapshot } from '@angular/router';
import { Token } from '../services/token';

@Injectable({
  providedIn: 'root',
})
export class RoleGuard implements CanActivate{

  constructor(
    private tokenService: Token,
    private router: Router
  ){}

  canActivate(route: ActivatedRouteSnapshot): boolean{

    const expectedRoles = route.data['roles'] as string[];
    const userRoles = this.tokenService.getRoles();
    
    const hasRole = userRoles.some(role => 
      expectedRoles.includes(role)
    );

    if (!hasRole){
      this.router.navigate(['/login']);
      return false;
    }

    return true;
  }
}