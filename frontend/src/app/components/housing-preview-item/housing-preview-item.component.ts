import {Component, Input, OnInit} from '@angular/core';
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";
import {HousingService} from "../../services/housing.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {coordinatesValidator} from "../../validators/Coordinates";
import {positiveNumberValidator} from "../../validators/PositiveNumber";
import {checkInCheckOutValidator} from "../../validators/CheckIn";
import {ILocation} from "../../models/ILocation";
import {HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-housing-preview-item',
  templateUrl: './housing-preview-item.component.html',
  styleUrls: ['./housing-preview-item.component.scss']
})
export class HousingPreviewItemComponent implements OnInit {
  @Input() housing!: IHousing;
  @Input() nights: number = 1;
  totalPrice: number = 0;

  constructor(
    private httpService: HttpService,
    private housingService: HousingService
  ) {}

  ngOnInit(): void {
    this.totalPrice = Math.round(this.housing.pricePerNight * this.nights * 100) / 100;
  }

  addToFavourites(housingInput: IHousing) {
    if (housingInput && housingInput.id) {
      const headers = new HttpHeaders({
        'Authorization': 'YourAuthToken' // Replace 'YourAuthToken' with the actual token
      });
      console.log(housingInput)
      console.log(housingInput.id)
      this.httpService.sendPostRequest(`/v1/favourites/${housingInput.id}`, {}).subscribe(
        response => {
          this.housingService.housing = response as IHousing;
          this.housingService.currentStep++;
        }, err => {
          console.error(err);
        }
      )
    }
  }

}
