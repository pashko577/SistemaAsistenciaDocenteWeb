// theme_service.ts
import { Injectable, signal } from '@angular/core';

export type Theme = 'light' | 'dark';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private currentThemeSignal = signal<Theme>('light');
  public currentTheme = this.currentThemeSignal.asReadonly();

  constructor() {
    this.initializeTheme();
    this.watchSystemTheme();
  }

  private initializeTheme(): void {
    const savedTheme = localStorage.getItem('theme') as Theme;
    
    let initialTheme: Theme;
    
    if (savedTheme === 'light' || savedTheme === 'dark') {
      initialTheme = savedTheme;
    } else {
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      initialTheme = prefersDark ? 'dark' : 'light';
    }
    
    console.log('🎨 Tema inicial:', initialTheme);
    this.setTheme(initialTheme);
  }

  private watchSystemTheme(): void {
    // Escuchar cambios en la preferencia del sistema
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
      if (!localStorage.getItem('theme')) {
        this.setTheme(e.matches ? 'dark' : 'light');
      }
    });
  }

  toggleTheme(): void {
    const newTheme = this.currentThemeSignal() === 'light' ? 'dark' : 'light';
    console.log('🔄 Cambiando tema a:', newTheme);
    this.setTheme(newTheme);
  }

  private setTheme(theme: Theme): void {
    this.currentThemeSignal.set(theme);
    localStorage.setItem('theme', theme);
    
    const htmlElement = document.documentElement;
    const bodyElement = document.body;
    
    if (theme === 'dark') {
      htmlElement.classList.add('dark');
      bodyElement.classList.add('dark');
      // Forzar repaint para asegurar que todos los componentes se actualicen
      this.forceRepaint();
    } else {
      htmlElement.classList.remove('dark');
      bodyElement.classList.remove('dark');
      this.forceRepaint();
    }
    
    console.log('✅ Tema aplicado globalmente:', theme);
  }

  private forceRepaint(): void {
    // Truco para forzar repaint en todos los componentes
    document.body.style.display = 'none';
    document.body.offsetHeight; // Trigger reflow
    document.body.style.display = '';
  }

  getCurrentTheme(): Theme {
    return this.currentThemeSignal();
  }

  isDarkMode(): boolean {
    return this.currentThemeSignal() === 'dark';
  }
}