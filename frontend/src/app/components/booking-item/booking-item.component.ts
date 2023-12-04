import {Component, EventEmitter, Input, Output} from '@angular/core';
import {IBooking} from "../../models/IBooking";
import {HttpService} from "../../services/http.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-booking-item',
  templateUrl: './booking-item.component.html',
  styleUrls: ['./booking-item.component.scss']
})
export class BookingItemComponent {
  @Input() booking!: IBooking;
  @Output() bookingDeleted = new EventEmitter<number>();

  constructor(private httpService: HttpService, private httpClient: HttpClient) {

  }


}
