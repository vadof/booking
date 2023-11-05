import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-review-dialog',
  templateUrl: './review-dialog.component.html',
  styleUrls: ['./review-dialog.component.scss']
})
export class ReviewDialogComponent {
  reviewText: string = ''; // Holds the text of the review

  constructor(
    public dialogRef: MatDialogRef<ReviewDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  submitReview(): void {
    // Handle the review submission here
    // You can send `this.reviewText` to your backend or use it as needed
    console.log(this.reviewText);
    this.dialogRef.close(this.reviewText);
  }

  // Implement your methods to handle the review submission
}
