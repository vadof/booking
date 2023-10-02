import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ILocation} from "../../models/ILocation";
import {LocationService} from "../../services/location.service";
import {HttpService} from "../../services/http.service";
import {HousingService} from "../../services/housing.service";
import {IHousing} from "../../models/IHousing";
import {positiveNumberValidator} from "../../validators/PositiveNumber";
import {checkInCheckOutValidator} from "../../validators/CheckIn";
import {coordinatesValidator} from "../../validators/Coordinates";

@Component({
  selector: 'app-housing-form',
  templateUrl: './housing-form.component.html',
  styleUrls: ['./housing-form.component.scss']
})
export class HousingFormComponent implements OnInit {

  locations: ILocation[] = [];
  errorMessage: string = '';

  constructor(private locationService: LocationService,
              private httpService: HttpService,
              private housingService: HousingService) {}

  ngOnInit(): void {
    this.locationService.getLocations().then(
      (response) => {
        this.locations = response;
        this.locations.sort((a, b) => a.name.localeCompare(b.name));
      }
    )
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

  addHousing() {
    if (this.housingForm.valid) {
      this.httpService.sendPostRequest('/v1/housings', this.getRequestObjectFromForm()).subscribe(
        response => {
          this.housingService.housing = response as IHousing;
          this.housingService.currentStep++;
        },
        error => {
          this.errorMessage = error.error;
        }
      )
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
      m2: this.housingForm.controls.m2.value,
      minNights: this.housingForm.controls.minNights.value,
    }
  }
}
