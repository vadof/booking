import { Component } from '@angular/core';
import {IBooking} from "../../models/IBooking";
import {HttpService} from "../../services/http.service";
import {BookingService} from "../../services/booking.service";

@Component({
  selector: 'app-booking-history-page',
  templateUrl: './booking-history-page.component.html',
  styleUrls: ['./booking-history-page.component.scss']
})
export class BookingHistoryPageComponent {
  bookings: IBooking[] = [];


  constructor(private httpService: HttpService, private bookingService: BookingService) {

  }

  ngOnInit(): void {
    this.httpService.sendGetRequest('/v1/bookings').subscribe(
      response => { // json
        this.bookings = response as IBooking[];
      }
    )
  }
}
