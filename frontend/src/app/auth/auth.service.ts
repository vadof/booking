import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthResponse} from "./AuthResponse";
import {Observable} from "rxjs";
import {API_URL} from "../config/constants";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loginUrl = `${API_URL}/v1/auth/login`
  private registerUrl = `${API_URL}/v1/auth/register`

  constructor(private http: HttpClient) {
  }

  public login(request: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.loginUrl, request, httpOptions);
  }

  public register(request: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.registerUrl, request, httpOptions);
  }
}
