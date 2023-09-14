import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainPageComponent} from "./pages/main-page/main-page.component";
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {AuthGuard} from "./auth/AuthGuard";
import {RegisterPageComponent} from "./pages/register-page/register-page.component";

const routes: Routes = [
  {path: '', component: MainPageComponent, canActivate: [AuthGuard]},
  {path: 'login', component: LoginPageComponent},
  {path: 'register', component: RegisterPageComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
