import {Component, Input, OnInit} from '@angular/core';
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";
import {HousingService} from "../../services/housing.service";
import {ILocation} from "../../models/ILocation";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {coordinatesValidator} from "../../validators/Coordinates";
import {positiveNumberValidator} from "../../validators/PositiveNumber";
import {checkInCheckOutValidator} from "../../validators/CheckIn";
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
  locations: ILocation[] = [];


  constructor(
    private httpService: HttpService,
    private housingService: HousingService
  ) {}

  ngOnInit(): void {
    this.totalPrice = Math.round(this.housing.pricePerNight * this.nights * 100) / 100;
  }

  housingForm = new FormGroup({
    name: new FormControl<string>('', Validators.required),
    location: new FormControl<string>('', Validators.required),
    coordinates: new FormControl<string>('', [Validators.required, coordinatesValidator]),
    pricePerNight: new FormControl<string>('', [Validators.required, positiveNumberValidator]),
    people: new FormControl<string>('', [Validators.required, positiveNumberValidator]),
    check: new FormControl<string>('', [Validators.required, checkInCheckOutValidator]),
    minRentalAge: new FormControl<string>('', [Validators.required, positiveNumberValidator]),
    description: new FormControl<string>('', Validators.required),
    rooms: new FormControl<string>('', [Validators.required]),
    m2: new FormControl<string>('', [Validators.required, positiveNumberValidator]),
    minNights: new FormControl<string>('', [Validators.required, positiveNumberValidator])
  })

  addToFavourites(housingInput: IHousing) {
    if (housingInput && housingInput.id) {
      const headers = new HttpHeaders({
        'Authorization': 'YourAuthToken' // Replace 'YourAuthToken' with the actual token
      });

      this.httpService.sendPostRequest(`/favourites/${housingInput.id}`, {}).subscribe(
        response => {
          // Assuming the response will be the updated housing object
          this.housingService.housing = response as IHousing;
          // Handle any additional logic or state updates here
          console.log('Added to favourites successfully');
        }, err => {
          console.error(err);
        }
      );
    }
  }

  private getRequestObjectFromForm(): Object {
    const locationId = +this.housingForm.controls.location.value!;
    const location: ILocation = this.locations.filter((l) => l.id === locationId)[0];

    const check: any = this.housingForm.controls.check.value?.split('-');
    const checkIn: string = check[0].trim() + ':00';
    const checkOut: string = check[1].trim() + ':00';

    return {
      name: this.housingForm.controls.name.value,
      location: location,
      coordinates: this.housingForm.controls.coordinates.value,
      pricePerNight: this.housingForm.controls.pricePerNight.value,
      people: this.housingForm.controls.people.value,
      checkIn,
      checkOut,
      minAgeToRent: this.housingForm.controls.minRentalAge.value,
      description: this.housingForm.controls.description.value,
      rooms: this.housingForm.controls.rooms.value,
      m2: Math.round(+this.housingForm.controls.m2.value!),
      minNights: this.housingForm.controls.minNights.value,
    }
  }

}
