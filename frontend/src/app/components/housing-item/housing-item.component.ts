import {Component, Input, OnInit} from '@angular/core';
import {IHousing} from "../../models/IHousing";
import {IImage} from "../../models/IImage";

@Component({
  selector: 'app-housing-item',
  templateUrl: './housing-item.component.html',
  styleUrls: ['./housing-item.component.scss']
})
export class HousingItemComponent implements OnInit {
  @Input({required: true}) housing!: IHousing;

  sideImages: IImage[] = [];
  bottomImages: IImage[] = [];

  ngOnInit(): void {
    this.housing.images.sort((a, b) => a.id - b.id);
    this.sideImages = this.housing.images.slice(0, 2);
    this.bottomImages = this.housing.images.slice(2, 4);
  }
}
