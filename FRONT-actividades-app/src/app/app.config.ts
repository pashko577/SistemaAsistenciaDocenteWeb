import { ApplicationConfig, importProvidersFrom, provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations'; // O provideAnimationsAsync()
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { AuthInterceptor } from './core/interceptors/auth-interceptor';
import { BookOpen, ChevronDown, Edit3, Layers, LucideAngularModule, Plus, Trash2, UserPlus, X } from 'lucide-angular';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([AuthInterceptor])

    ),

    importProvidersFrom(
      LucideAngularModule.pick({
        Plus,
        Trash2,
        Edit3,
        X,
        UserPlus,
        BookOpen,
        Layers,
        ChevronDown
      })
    )

  ]
};
