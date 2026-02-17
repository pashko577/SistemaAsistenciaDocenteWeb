import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UserHeader } from '../../pages/user-header/user-header';

@Component({
  selector: 'app-user-layout',
  imports: [RouterOutlet,UserHeader],
  templateUrl: './user-layout.html',
  styleUrl: './user-layout.css',
})
export class UserLayout {
}
