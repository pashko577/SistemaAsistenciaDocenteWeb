import { Component, signal } from '@angular/core';
import { Auth } from '../../../../core/services/auth';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Token } from '../../../../core/services/token';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  loginForm!: FormGroup;
  loading = false;
  backendError = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private authService: Auth,
    private TokenService: Token,
    private router: Router
  ) {
    this.createForm();
  }

  private createForm(): void {
    this.loginForm = this.fb.group({
      dni: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

// features/auth/pages/login/login.ts

// features/auth/pages/login/login.ts

login() {
  if (this.loginForm.invalid) {
    this.loginForm.markAllAsTouched();
    return;
  }

  this.loading = true;
  this.backendError.set(null);

  this.authService.login(this.loginForm.value)
    .subscribe({
      next: (res) => {
        this.TokenService.saveToken(res.token);
        this.TokenService.saveRoles(res.roles);
        this.TokenService.saveUser(res.dni);
        
        // ✅ CORRECCIÓN: Usar 'rutas_permitidas' que es lo que envía el Backend
        if (res.rutas_permitidas) {
          // Extraemos solo los strings de las rutas (ej: ["/dashboard", "/gestion-docente"])
          const rutas = res.rutas_permitidas.map((m: any) => m.ruta);
          localStorage.setItem('rutas_permitidas', JSON.stringify(rutas));
          console.log('Rutas guardadas:', rutas); // Para que verifiques en consola
        }

        this.loading = false;

        if (res.roles && res.roles.length > 0) {
          console.log('Login exitoso. Redirigiendo a Dashboard Global');
          this.router.navigate(['/dashboard']);
        } else {
          this.router.navigate(['/login']);
        }
      },
      error: err => {
        this.loading = false;
        this.backendError.set('DNI o contraseña incorrectos');
      },
    });
}
}
