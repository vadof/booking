import {MatDateRangePicker} from "@angular/material/datepicker";
import {FormControl, FormGroup} from "@angular/forms";
import {LocationService} from "../../services/location.service";
import {Component, OnInit, ViewChild} from "@angular/core";
import {ILocation} from "../../models/ILocation";
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";
import {IHousingPaginatedResponse} from "../../reponses/IHousingPaginatedResponse";


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
        console.log(response)
        this.housings = response as IHousing[];
        console.log(this.housings)
      }
    )
  }
}
