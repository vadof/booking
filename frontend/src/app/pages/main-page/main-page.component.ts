import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpService} from "../../services/http.service";
import {IHousing} from "../../models/IHousing";
import {ILocation} from "../../models/ILocation";
import {MatDateRangePicker} from "@angular/material/datepicker";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss']
})
export class MainPageComponent implements OnInit {

  locations: ILocation[] = [
    {id: 1, name: 'Tallinn'},
    {id: 1, name: 'Tqiwan'},
    {id: 1, name: 'Twhuana'},
    {id: 4, name: 'Tri'},
    {id: 2, name: 'Narva'},
    {id: 3, name: 'Rakvere'}
  ]
  housings: IHousing[] = [];

  locationText: string = '';
  locationWindow: boolean = false;
  selectedLocation: ILocation | null = null;

  @ViewChild('picker') datePicker!: MatDateRangePicker<Date>;

  range = new FormGroup({
    start: new FormControl<Date | null>(new Date()),
    end: new FormControl<Date | null>(null),
  });

  constructor(
    private httpService: HttpService
  ) {
  }

  ngOnInit(): void {
    // let location: ILocation = {
    //   id: 1,
    //   name: 'Tallinn'
    // }
    //
    // let housing: IHousing = {
    //   id: 1,
    //   name: 'Bob W Avangard',
    //   location,
    //   housingDetails: null,
    //   coordinates: '53.32131, 36.32134',
    //   pricePerNight: 54.32,
    //   people: 2,
    //   rating: 8.4
    // }
    //
    // this.housings.push(housing);
  }

  info() {
    this.datePicker.open();
    console.log(this.range.controls.start.value)
    console.log(this.range.controls.end.value)
  }

  changeLocationWindow() {
    setTimeout(() => {
      this.locationWindow = !this.locationWindow;
    }, 200);
  }

  myFilter = (d: Date | null): boolean => {
    if (!d) {
      d = new Date();
    }

    const currentDate = new Date();
    const yesterday = new Date(currentDate.getTime() - 24 * 60 * 60 * 1000);

    return d > new Date() || d > yesterday;
  };
}
