import { Component } from '@angular/core';
import { UserHeader } from "../../../pages/user-header/user-header";
import { RouterOutlet } from "@angular/router";

@Component({
  selector: 'app-user-layout',
  imports: [UserHeader, RouterOutlet],
  templateUrl: './user-layout.html',
  styleUrl: './user-layout.css',
})
export class UserLayout {

}
