import {MatDateRangePicker} from "@angular/material/datepicker";
import {FormControl, FormGroup} from "@angular/forms";
import {LocationService} from "../../services/location.service";
import {Component, OnInit, ViewChild} from "@angular/core";
import {ILocation} from "../../models/ILocation";
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss']
})
export class MainPageComponent implements OnInit {

  locations: ILocation[] = [];
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
    private httpService: HttpService,
    private locationService: LocationService
  ) {
  }

  ngOnInit(): void {
    this.locationService.getLocations().then(
      locations => {this.locations = locations;}
    )
    this.httpService.sendGetRequest('/v1/housings').subscribe(
      response => {
        this.housings = response as IHousing[];
      }
    )
    this.setLocationWindow();
    this.trackDateValues();
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
