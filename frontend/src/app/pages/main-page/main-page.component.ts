import {MatDateRangePicker} from "@angular/material/datepicker";
import {FormControl, FormGroup} from "@angular/forms";
import {LocationService} from "../../services/location.service";
import {Component, OnInit, ViewChild} from "@angular/core";
import {ILocation} from "../../models/ILocation";
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";
import {IHousingPaginatedResponse} from "../../responses/IHousingPaginatedResponse";
import {SortType} from "../../enums/SortType";
import {DateService} from "../../services/date.service";

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

  minPrice: number = 0
  maxPrice: number = 0
  priceRange: PriceRange | null = null;

  sorting: SortType | null = null;

  @ViewChild('picker') datePicker!: MatDateRangePicker<Date>;

  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });

  constructor(
    private httpService: HttpService,
    private locationService: LocationService,
    private dateService: DateService
  ) {
  }

  ngOnInit(): void {
    this.locationService.getLocations().then(
      locations => {
        this.locations = locations;
      }
    )

    this.setSliderPrices();
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

      if (start && end) {
        this.dateService.checkInDate = start;
        this.dateService.checkOutDate = end;
      }
    })
  }

  private setSliderPrices(): void {
    this.httpService.sendGetRequest('/v1/housings/prices').subscribe(res => {
      const price: PriceRange = res as PriceRange;
      this.priceRange = price;
      this.minPrice = price.min;
      this.maxPrice = price.max;
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

    if (this.minPrice) params.push(`minPrice=${this.minPrice}`)
    if (this.maxPrice) params.push(`maxPrice=${this.maxPrice}`)

    if (this.sorting) {
      if (this.sorting === SortType.MOST_POPULAR) params.push(`sortingFields=rating&sortDirection=DESC`)
      else if (this.sorting === SortType.NEWEST) params.push(`sortDirection=DESC`)
      else if (this.sorting === SortType.PRICE_HIGHEST) params.push(`sortingFields=pricePerNight&sortDirection=DESC`)
      else if (this.sorting === SortType.PRICE_LOWEST) params.push(`sortingFields=pricePerNight&sortDirection=ASC`)
    }

    for (let i = 0; i < params.length; i++) {
      path += params[i];
      if (i !== params.length - 1) path += '&';
    }

    this.httpService.sendGetRequest(path).subscribe(
      response => {
        const paginatedResponse: IHousingPaginatedResponse = response as IHousingPaginatedResponse;
        this.totalPages = paginatedResponse.totalPages;
        this.housings = paginatedResponse.data;
        this.pages = Array.from({length: this.totalPages}, (_, i) => i);
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

  sortHousings(sorting: SortType) {
    this.sorting = sorting;
    this.searchHousings();
  }

  protected readonly SortType = SortType;
}

interface PriceRange {
  min: number;
  max: number;
}
