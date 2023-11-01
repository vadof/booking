import {Component, OnInit} from '@angular/core';
import {IBooking} from "../../models/IBooking";
import {HttpService} from "../../services/http.service";
import {BookingService} from "../../services/booking.service";
import {IHousingPaginatedResponse} from "../../reponses/IHousingPaginatedResponse";
import {Observable} from "rxjs";

@Component({
  selector: 'app-booking-history-page',
  templateUrl: './booking-history-page.component.html',
  styleUrls: ['./booking-history-page.component.scss']
})
export class BookingHistoryPageComponent implements OnInit{
  bookings: IBooking[] = [];


  constructor(private httpService: HttpService, private bookingService: BookingService) {

  }

  ngOnInit(): void {
    this.httpService.sendGetRequest('/v1/bookings').subscribe(
      response => { // json
        let response1 = response as IHousingPaginatedResponse
        this.bookings = response1.data as IBooking[]
        console.log(this.bookings)
      }
    )
  }
  cancelBooking(bookingId: number) {
    return this.httpService.sendDeleteRequest(`/v1/bookings/${bookingId}`).subscribe(
      response => {
        let response1 = response as IBooking
        this.handleBookingDeleted(response1.id)
      }
    );
  }

  canDeleteBooking(booking: IBooking): boolean {
    const currentDate = new Date();
    const parsedDate = this.parseDate(booking.checkInDate);

    if (!parsedDate) {
      // Handle this case as you see fit, e.g., by defaulting to "cannot delete":
      return false;
    }

    const checkInDate = new Date(parsedDate);

    currentDate.setHours(0, 0, 0, 0);
    checkInDate.setHours(0, 0, 0, 0);

    return currentDate.getTime() !== checkInDate.getTime();
  }

  parseDate(dateString: string): Date | null {
    const parts = dateString.split('/');
    if (parts.length !== 3) {
      return null; // Invalid format
    }

    const day = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10) - 1; // Month is zero-indexed in JavaScript
    const year = parseInt(parts[2], 10);

    if (isNaN(day) || isNaN(month) || isNaN(year)) {
      return null; // Not a valid date
    }

    return new Date(year, month, day);
  }

  handleBookingDeleted(bookingId: number) {
    this.bookings = this.bookings.filter(booking => booking.id !== bookingId);
  }
}
