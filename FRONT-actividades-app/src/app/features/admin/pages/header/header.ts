import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Token } from '../../../../core/services/token';
import { Auth } from '../../../../core/services/auth';

@Component({
  selector: 'app-header',
  imports: [CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {

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
