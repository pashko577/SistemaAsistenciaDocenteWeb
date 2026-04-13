import { Component } from '@angular/core';

@Component({
  selector: 'app-user-header',
  imports: [],
  templateUrl: './user-header.html',
  styleUrl: './user-header.css',
})
export class UserHeader {

  hasAccess(route: string): boolean {
    const permitidas = localStorage.getItem('rutas_permitidas');
    if (!permitidas) return false;
    
    // Convertimos el string del localStorage a un array real
    const routesArray: string[] = JSON.parse(permitidas);
    return routesArray.includes(route);
  }
}