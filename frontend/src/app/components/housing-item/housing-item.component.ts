import {Component, Input, OnInit} from '@angular/core';
import {IHousing} from "../../models/IHousing";

@Component({
  selector: 'app-housing-item',
  templateUrl: './housing-item.component.html',
  styleUrls: ['./housing-item.component.scss']
})
export class HousingItemComponent implements OnInit {
  @Input() housing!: IHousing;
  @Input() nights: number = 1;
  totalPrice: number = 0;

  ngOnInit(): void {
    this.totalPrice = Math.round(this.housing.pricePerNight * this.nights * 100) / 100;
  }
}
