import { Component, signal } from '@angular/core';
import { EmailValidator, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Auth } from '../../../../core/services/auth';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  registerForm! : FormGroup;
  loading = false;
  successMessage = '';
  backendError = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private authService: Auth
  ){
    this.createForm();
  }

  private createForm(): void{
    this.registerForm = this.fb.group({
      email: ['',[Validators.required, Validators.email]],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  register(): void{
    if(this.registerForm.invalid){
      this.registerForm.markAllAsTouched();
      return;
    }

    this.backendError.set(null);
    this.successMessage = '';
    this.loading = true;

    const request = {
      ...this.registerForm.value,
      roles: ['USER']
    }

    this.authService.register(request).subscribe({
      next: () =>{
        this.successMessage = 'Usuario registrado correctamente';
        this.registerForm.reset();
        this.loading = false;
      },
      error: err =>{
        const backendMsg =
          typeof err.error === 'string'
          ? err.error
          : err.error?.message;

        if (backendMsg === 'USERNAME_ALREADY_EXISTS') {
          this.backendError.set('El nombre de usuario ya existe')
        } else if (backendMsg === 'EMAIL_ALREADY_EXISTS'){
          this.backendError.set('El correo ya está registrado');
        } else {
          this.backendError.set('Error al registrar usuario');
        }
        
        this.loading = false;
        
      }
    });

  }
}
