import {Component, Input, OnInit} from '@angular/core';
import {IHousing} from "../../models/IHousing";

@Component({
  selector: 'app-housing-preview-item',
  templateUrl: './housing-preview-item.component.html',
  styleUrls: ['./housing-preview-item.component.scss']
})
export class HousingPreviewItemComponent implements OnInit {
  @Input() housing!: IHousing;
  @Input() nights: number = 1;
  totalPrice: number = 0;

  ngOnInit(): void {
    this.totalPrice = Math.round(this.housing.pricePerNight * this.nights * 100) / 100;
  }
}
