import { Component } from '@angular/core';
import {ILocation} from "../../models/ILocation";
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";
import {LocationService} from "../../services/location.service";

@Component({
  selector: 'app-my-favourites-page',
  templateUrl: './my-favourites-page.component.html',
  styleUrls: ['./my-favourites-page.component.scss']
})
export class MyFavouritesPageComponent {
  locations: ILocation[] = [];
  housings: IHousing[] = [];

  constructor(
    private httpService: HttpService,
    private locationService: LocationService
  ) {
  }

  ngOnInit() {
    this.locationService.getLocations().then(
      locations => {this.locations = locations;}
    )
    this.httpService.sendGetRequest('/api/v1/favourites').subscribe(
      response => {
        this.housings = response as IHousing[];
      }
    )
  }
}
