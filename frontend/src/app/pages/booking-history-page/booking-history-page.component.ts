import {Component, OnInit} from '@angular/core';
import {IBooking} from "../../models/IBooking";
import {HttpService} from "../../services/http.service";
import {IHousingPaginatedResponse} from "../../responses/IHousingPaginatedResponse";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";


@Component({
  selector: 'app-booking-history-page',
  templateUrl: './booking-history-page.component.html',
  styleUrls: ['./booking-history-page.component.scss']
})
export class BookingHistoryPageComponent implements OnInit {
  bookings: IBooking[] = [];

  showReview: number = 0;
  errorMessage: string = '';

  reviewForm = new FormGroup({
    text: new FormControl<string>('', Validators.required),
    rating: new FormControl<number>(1, Validators.required)
  })

  constructor(private httpService: HttpService, private router: Router) {

  }

  ngOnInit(): void {
    this.httpService.sendGetRequest('/v1/bookings').subscribe(
      response => { // json
        let response1 = response as IHousingPaginatedResponse
        this.bookings = response1.data as IBooking[]
      }
    )
  }

  addReview(booking: IBooking) {
    if (this.reviewForm.valid) {
      let obj = {
        text: this.reviewForm.controls.text.value,
        rating: this.reviewForm.controls.rating.value
      }

      this.httpService.sendPostRequest(`/v1/reviews/${booking.housing.id}`, obj).subscribe(
        () => this.router.navigate([`/housing/${booking.housing.id}`]),
          err => this.errorMessage = err.error
      )
    }
  }

  cancelBooking(bookingId: number) {
    return this.httpService.sendDeleteRequest(`/v1/bookings/${bookingId}`).subscribe(
      response => {
        let response1 = response as IBooking
        this.handleBookingDeleted(response1.id)
      }
    );
  }

  openReviewArea(booking: IBooking) {
    this.showReview = booking.id;
    this.errorMessage = '';
  }

  canDeleteBooking(booking: IBooking): boolean {
    const currentDate = new Date();
    const bookingDate = this.parseDate(booking.checkInDate);

    if (!bookingDate) {
      return false;
    }

    const checkInDate = new Date(bookingDate);

    return currentDate > checkInDate;
  }

  parseDate(dateString: string): Date | null {
    const parts = dateString.split('/');
    if (parts.length !== 3) {
      return null; // Invalid format
    }

    const day = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10) - 1;
    const year = parseInt(parts[2], 10);

    if (isNaN(day) || isNaN(month) || isNaN(year)) {
      return null;
    }

    return new Date(year, month, day);
  }

  handleBookingDeleted(bookingId: number) {
    this.bookings = this.bookings.filter(booking => booking.id !== bookingId);
  }

  closeReviewArea() {
    this.showReview = 0;
    this.errorMessage = '';
  }

  canAddReview(booking: IBooking): boolean {
    let date = this.parseDate(booking.checkOutDate);

    if (date) {
      return date <= new Date();
    }
    return false;
  }
}
