import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    MatButtonModule,
    MatDividerModule,
    MatIconModule,
    CommonModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})



export class AppComponent {

  loggedIn = true;

  constructor() {
  }

  login(){
    this.loggedIn = true;
  }

  logout(){
    this.loggedIn = false;
  }

  title = 'oglasi-app';
}
