import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "../../auth/token-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  email: string | null = '';

  constructor(
    private tokenStorage: TokenStorageService,
    private router: Router
    ) {
  }

  logout() {
    this.tokenStorage.signOut();
    this.router.navigate(['/login'])
  }

  ngOnInit(): void {
    this.email = this.tokenStorage.getEmail();
  }

}
