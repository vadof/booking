import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {HttpService} from "./http.service";
import {IReviewDTO} from "../models/IReviewDTO";
import {getNumberOfCurrencyDigits} from "@angular/common";
import {IBooking} from "../models/IBooking"; // Assuming you have a ReviewDTO interface

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor(private http: HttpClient, private httpService: HttpService) {}


  async postReview(data: IBooking, text: any, id: number): Promise<IReviewDTO> {
    return await new Promise<IReviewDTO>((resolve, reject) => {
      const reviewDTO: IReviewDTO = {
        id: id, // Assuming id is the same as the one passed to the method
        text: text, // New text to overwrite
        rating: 5,
        date: new Date(),
        reviewer: data.tenant// Set a default or actual rating value
      };
      console.log(reviewDTO)
      this.httpService.sendPostRequest(`/v1/reviews/${id}`, reviewDTO).subscribe(response => {
        resolve(response);
      }, error => {
        reject(error);
      })
    })
  }
}
