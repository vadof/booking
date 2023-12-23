import {Injectable} from '@angular/core';
import {HttpService} from './http.service';
import {HttpClient} from "@angular/common/http";
import {IUserReview} from "../models/IUserReview";

@Injectable({
  providedIn: 'root'
})
export class UserReviewService {


  constructor(private http: HttpClient, private httpService: HttpService) {
  }

  async postReview(data: IUserReview): Promise<IUserReview> {
    return await new Promise<IUserReview>((resolve, reject) => {

      this.httpService.sendPostRequest(`/v1/user-review`, data).subscribe(response => {
        resolve(response);
      }, error => {
        reject(error);
      })
    })
  }
}
