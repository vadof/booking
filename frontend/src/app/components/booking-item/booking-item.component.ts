import {Component, Input} from '@angular/core';
import {IBooking} from "../../models/IBooking";

@Component({
  selector: 'app-booking-item',
  templateUrl: './booking-item.component.html',
  styleUrls: ['./booking-item.component.scss']
})
export class BookingItemComponent {
  @Input() booking!:IBooking;

}
