import {MatDateRangePicker} from "@angular/material/datepicker";
import {FormControl, FormGroup} from "@angular/forms";
import {LocationService} from "../../services/location.service";
import {Component, OnInit, ViewChild} from "@angular/core";
import {ILocation} from "../../models/ILocation";
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";
import {IHousingPaginatedResponse} from "../../reponses/IHousingPaginatedResponse";
import {start} from "@popperjs/core";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss']
})
export class MainPageComponent implements OnInit {

  currentPage: number = 0;
  totalPages: number = 0;
  contentSize: number = 10;

  pages: number[] = []
  locations: ILocation[] = [];
  housings: IHousing[] = [];

  locationText: string = '';
  locationWindow: boolean = false;
  selectedLocation: ILocation | null = null;

  peopleWindow: boolean = false;
  peopleAmount: number = 2;
  roomAmount: number = 1;

  startingPrice: number = 0
  maximumPrice: number = 0

  sortByPriceLowest: boolean = false
  sortByPriceHighest: boolean = false

  sortByMostPopular: boolean = false

  sortByNewest: boolean = false

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

    this.searchHousings();
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

  searchHousings() {
    console.log(this.startingPrice)
    let path = '/v1/housings?';
    let params: string[] = [];
    params.push(`page=${this.currentPage}`)
    params.push(`size=${this.contentSize}`)
    params.push(`people=${this.peopleAmount}`)
    params.push(`rooms=${this.roomAmount}`)
    if (this.selectedLocation) params.push(`location=${this.selectedLocation.name}`);

    const startDate = this.range.controls.start.value;
    const endDate = this.range.controls.end.value;
    if (startDate && endDate) {
      params.push(`checkInDate=${this.dateToFormattedString(startDate)}`);
      params.push(`checkOutDate=${this.dateToFormattedString(endDate)}`)
    }

    // TODO add price filter section
    if (this.startingPrice) params.push(`minPrice=${this.startingPrice}`)
    if (this.maximumPrice) params.push(`maxPrice=${this.maximumPrice}`)

    // TODO add sorting selection
    if (this.sortByPriceHighest) params.push(`sortingFields=pricePerNight&sortDirection=DESC`)
    if (this.sortByPriceLowest) params.push(`sortingFields=pricePerNight&sortDirection=ASC`)

    if (this.sortByMostPopular) params.push(`sortingFields=rating&sortDirection=DESC`)

    if (this.sortByNewest) params.push(`sortDirection=DESC`)

    for (let i = 0; i < params.length; i++) {
      path += params[i];
      if (i !== params.length - 1) path += '&';
    }

    console.log(path)

    this.httpService.sendGetRequest(path).subscribe(
      response => {
        const paginatedResponse: IHousingPaginatedResponse = response as IHousingPaginatedResponse;
        this.totalPages = paginatedResponse.totalPages;
        this.housings = paginatedResponse.data;
        this.pages = Array.from({ length: this.totalPages }, (_, i) => i);
      }
    )
  }

  changePage(page: number) {
    this.currentPage = page;
    this.searchHousings();
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

  private dateToFormattedString(date: Date) {
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear().toString();

    return `${day}/${month}/${year}`;
  }

  sortHousingsHighestPrice() {
    this.sortByPriceHighest = true
    this.sortByPriceLowest = false
    this.sortByMostPopular = false
    this.sortByNewest = false
    this.searchHousings()
  }

  sortHousingsLowestPrice() {
    this.sortByPriceHighest = false
    this.sortByPriceLowest = true
    this.sortByMostPopular = false
    this.sortByNewest = false
    this.searchHousings()
  }

  sortHousingsMostPopular() {
    this.sortByPriceHighest = false
    this.sortByPriceLowest = false
    this.sortByMostPopular = true
    this.sortByNewest = false
    this.searchHousings()
  }

  sortHousingsNewest() {
    this.sortByPriceHighest = false
    this.sortByPriceLowest = false
    this.sortByMostPopular = false
    this.sortByNewest = true
    this.searchHousings()
  }
}
