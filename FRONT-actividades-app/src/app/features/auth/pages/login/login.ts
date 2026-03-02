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
        this.loading = false;

        if (res.roles.includes('ADMIN')) {
          this.router.navigate(['/admin']);
          console.log('Login ADMIN exitoso');
        } else if (res.roles.includes('DOCENTE')) {
          this.router.navigate(['/DOCENTE']);
          console.log('Login DOCENTE exitoso');
        } else if (res.roles.includes('ADMINISTRATIVO')) {
          this.router.navigate(['/ADMINISTRATIVO']);
          console.log('Login ADMINISTRATIVO exitoso');
        }
         else {
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
