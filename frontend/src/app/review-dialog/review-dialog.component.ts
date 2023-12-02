import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReviewService } from '../services/review.service';
import { IReviewDTO } from '../models/IReviewDTO';
import {Router} from "@angular/router";
import {IBooking} from "../models/IBooking";




@Component({
  selector: 'app-review-dialog',
  templateUrl: './review-dialog.component.html',
  styleUrls: ['./review-dialog.component.scss']
})
export class ReviewDialogComponent {
  reviewText: string = '';
  private reviewId: any;

  constructor(
    public dialogRef: MatDialogRef<ReviewDialogComponent>,
    private router: Router,
    private reviewService: ReviewService,
    @Inject(MAT_DIALOG_DATA) public data: any) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  submitReview(data: IBooking): void {
    console.log(data["checkInDate"])
    let id: number = data["id"];

    console.log(id)
    console.log(JSON.stringify(data))
    console.log(data.id)
    this.reviewService.postReview(data, this.reviewText, data.id).then((reviewText) => this.reviewText = reviewText.text)
      .catch(() => this.router.navigate(['']));
    this.dialogRef.close();

  }

}
