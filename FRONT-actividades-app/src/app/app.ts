import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ThemeService } from './core/services/theme_service';

@Component({
  selector: 'app-root',
  standalone: true, // Asegúrate de que sea standalone si usas imports
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'FRONT-actividades-app';

  // Al inyectar el servicio, su constructor se ejecuta 
  // y el 'effect' aplica el tema automáticamente.
  constructor(public themeService: ThemeService) {}
}