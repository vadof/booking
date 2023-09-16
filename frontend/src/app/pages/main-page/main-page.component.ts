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

  peopleWindow: boolean = false;
  peopleAmount: number = 2;
  roomAmount: number = 1;

  @ViewChild('picker') datePicker!: MatDateRangePicker<Date>;

  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });

  constructor(
    private httpService: HttpService
  ) {
  }

  ngOnInit(): void {
    this.setLocationWindow();
    this.trackDateValues();
    let housing: IHousing = {
      id: 1,
      name: 'Bob W Avangard',
      location: this.locations[0],
      housingDetails: null,
      imageSrc: 'https://cf.bstatic.com/xdata/images/hotel/max1024x768/473281457.jpg?k=bf5dfaf19efffb5b75ef7da11e8ad1bca51aa1eaa65e25ee367a1eb8d339b4ea&o=&hp=1',
      coordinates: '53.32131, 36.32134',
      pricePerNight: 54.32,
      people: 2,
      rating: 8.4
    }

    let housing1: IHousing = {
      id: 1,
      name: 'Grand Hotel',
      location: this.locations[4],
      housingDetails: null,
      imageSrc: 'https://static.independent.co.uk/2023/03/24/09/Best%20New%20York%20boutique%20hotels.jpg?width=1200',
      coordinates: '53.32131, 36.32134',
      pricePerNight: 117.17,
      people: 3,
      rating: 9.3
    }

    let housing2: IHousing = {
      id: 1,
      name: 'The Monks Bunk',
      location: this.locations[0],
      housingDetails: null,
      imageSrc: 'https://www.hotel-lapad.hr/media/qnsfcovf/nrl_6726.jpg?mode=min&width=1920&rnd=132885638239970000',
      coordinates: '53.32131, 36.32134',
      pricePerNight: 565,
      people: 3,
      rating: 9.9
    }

    this.housings.push(housing);
    this.housings.push(housing1);
    this.housings.push(housing2);
  }

  private setLocationWindow() {
    const peopleOptions = document.getElementById('peopleOptions')!;
    document.onclick = (e) => {
      if (!peopleOptions.contains(e.target as Node)) {
        this.peopleWindow = false;
      }
    }
  }

  private trackDateValues() {
    this.range.valueChanges.subscribe((controls) => {
      const start = controls.start;
      const end = controls.end;

      if (start && end && start >= end) {
        end.setDate(end.getDate() + 1);
      }
    })
  }

  setRoomAmount(value: number) {
    if (value >= 1) {
      this.roomAmount = value;
    }
  }

  setPeopleAmount(value: number) {
    if (value >= 1) {
      this.peopleAmount = value;
    }
  }

  info() {

  }

  openLocationWindow(status: boolean) {
    if (status) {
      this.locationWindow = status;
    } else {
      setTimeout(() => {
        this.locationWindow = status;
      }, 200);
    }

  }

  dateFilter = (d: Date | null): boolean => {
    if (!d) {
      d = new Date();
    }

    const currentDate = new Date();
    const yesterday = new Date(currentDate.getTime() - 24 * 60 * 60 * 1000);

    return d > new Date() || d > yesterday;
  };
}
