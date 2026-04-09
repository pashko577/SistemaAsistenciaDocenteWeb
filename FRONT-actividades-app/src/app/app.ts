import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ThemeService } from './core/services/theme_service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected title = 'FRONT-actividades-app';

  
    constructor(public themeService: ThemeService) {}
  
  ngOnInit(): void {
    // Asegurar que el tema se aplique al iniciar
    const currentTheme = this.themeService.getCurrentTheme();
    document.documentElement.classList.toggle('dark', currentTheme === 'dark');
    document.body.classList.toggle('dark', currentTheme === 'dark');
  }
}
