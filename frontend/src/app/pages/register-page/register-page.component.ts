import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../auth/auth.service";
import {TokenStorageService} from "../../auth/token-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.scss']
})
export class RegisterPageComponent {

  constructor(
    private authService: AuthService,
    private storage: TokenStorageService,
    private router: Router
  ) {
  }

  errorMessage: string = '';

  registerForm = new FormGroup({
    firstname: new FormControl<string>('', Validators.required),
    lastname: new FormControl<string>('', Validators.required),
    email: new FormControl<string>('',
      [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]),
    password: new FormControl<string>('',
      [Validators.required, Validators.minLength(6)]),
  })

  register() {
    if (this.registerForm.valid) {
      const registerRequest = {
        firstname: this.registerForm.value.firstname as string,
        lastname: this.registerForm.value.lastname as string,
        email: this.registerForm.value.email as string,
        password: this.registerForm.value.password as string
      }

      this.authService.register(registerRequest).subscribe(
        response => {
          this.storage.saveToken(response.token);
          this.storage.saveEmail(this.registerForm.value.email as string);
          this.router.navigate(['']);
        },
        error => {
          console.log(error);
        })
    } else {
      this.errorMessage = 'Fill in the empty fields!';
    }
  }

}
