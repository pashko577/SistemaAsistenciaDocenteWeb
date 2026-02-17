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
      username: ['', Validators.required],
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
        this.TokenService.saveRoles(res.Roles);
        this.TokenService.saveUser(res.Username);
        this.loading = false;

        if (res.Roles.includes('ADMIN')) {
          this.router.navigate(['/admin']);
          console.log('Login ADMIN exitoso');
        } else if (res.Roles.includes('USER')) {
          this.router.navigate(['/user']);
          console.log('Login USER exitoso');
        } else {
          this.router.navigate(['/login']);
        }
      },
      error: err => {
        this.loading = false;
        this.backendError.set('Usuario o contraseña incorrectos');
      },
    });
  }
}
