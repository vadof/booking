import {LocationService} from "../../services/location.service";
import {Component, OnInit} from "@angular/core";
import {ILocation} from "../../models/ILocation";
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";


@Component({
  selector: 'app-my-publications-page',
  templateUrl: './my-publications-page.component.html',
  styleUrls: ['./my-publications-page.component.scss']
})

export class MyPublicationsPageComponent implements OnInit{

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
    this.httpService.sendGetRequest('/v1/housings/my').subscribe(
      response => {
        this.housings = response as IHousing[];
      }
    )
  }
}
