import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { Observable } from 'rxjs';
import {IUser} from "../models/IUser";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private usersEndpoint = '/users'; // Adjust if your endpoint is different

  constructor(private httpService: HttpService) {}

  // Method to get a user by ID
  getUserById(id: number): Observable<IUser> {
    return this.httpService.sendGetRequest(`${this.usersEndpoint}/${id}`);
  }

  // Method to create a new user
  createUser(user: IUser): Observable<IUser> {
    return this.httpService.sendPostRequest(this.usersEndpoint, user);
  }

  // Method to update a user
  updateUser(user: IUser): Observable<IUser> {
    return this.httpService.sendPutRequest(`${this.usersEndpoint}/${user.id}`, user);
  }

  // Method to delete a user
  deleteUser(id: number): Observable<any> {
    return this.httpService.sendDeleteRequest(`${this.usersEndpoint}/${id}`);
  }

  // Add other user-related methods as needed
}
