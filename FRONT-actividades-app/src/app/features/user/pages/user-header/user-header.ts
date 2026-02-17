import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Token } from '../../../../core/services/token';
import { Auth } from '../../../../core/services/auth';

@Component({
  selector: 'app-user-header',
  imports: [CommonModule],
  templateUrl: './user-header.html',
  styleUrl: './user-header.css',
})
export class UserHeader {

  username: string | null = '';
  roles: string[] = [];

  constructor(
    private tokenService: Token,
    private authService: Auth
  ){
    this.username = this.tokenService.getUser();
    this.roles = this.tokenService.getRoles();
  }

  logout(): void{
    this.authService.logout();
  }

}
