import { Injectable, signal, effect, computed } from '@angular/core';

export type Theme = 'light' | 'dark';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  // Señal privada para manejar el estado interno
  private themeSignal = signal<Theme>(this.getStoredTheme());

  // Propiedades públicas reactivas
  public currentTheme = this.themeSignal.asReadonly();
  public isDarkMode = computed(() => this.themeSignal() === 'dark');

  constructor() {
    // Sincronización automática con el DOM y LocalStorage
    effect(() => {
      const theme = this.themeSignal();
      const root = document.documentElement;
      
      root.classList.toggle('dark', theme === 'dark');
      localStorage.setItem('theme', theme);
    });

    this.watchSystemTheme();
  }

  private getStoredTheme(): Theme {
    const saved = localStorage.getItem('theme') as Theme;
    if (saved === 'light' || saved === 'dark') return saved;
    
    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
  }

  private watchSystemTheme(): void {
    window.matchMedia('(prefers-color-scheme: dark)')
      .addEventListener('change', (e) => {
        // Solo cambiamos automáticamente si el usuario no ha fijado una preferencia manual
        if (!localStorage.getItem('theme')) {
          this.setTheme(e.matches ? 'dark' : 'light');
        }
      });
  }

  public toggleTheme(): void {
    this.setTheme(this.themeSignal() === 'light' ? 'dark' : 'light');
  }

  public setTheme(theme: Theme): void {
    this.themeSignal.set(theme);
  }
}