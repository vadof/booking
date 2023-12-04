import {Component} from '@angular/core';
import {TokenStorageService} from "../../auth/token-storage.service";
import {Router} from "@angular/router";
import {AuthService} from "../../auth/auth.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent {
  constructor(
    private authService: AuthService,
    private router: Router,
    private storage: TokenStorageService
  ) {
  }

  public errorMessage = ''

  loginForm = new FormGroup({
    email: new FormControl<string>('', Validators.required),
    password: new FormControl<string>('', Validators.required)
  })

  login() {
    if (this.loginForm.valid) {
      const email: string = this.loginForm.value.email as string;
      const password: string = this.loginForm.value.password as string;

      const loginRequest = {
        email,
        password
      }

      this.authService.login(loginRequest).subscribe(
        response => {
          this.storage.saveToken(response.token);
          this.storage.saveEmail(email);
          this.router.navigate(['']);
        },
        error => {
          this.errorMessage = 'Invalid Email or Password!';
          this.loginForm.reset();
        }
      )
    } else {
      this.errorMessage = 'Fill in the empty fields!'
    }
  }
}
