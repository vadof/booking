import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HousingService} from "../../services/housing.service";
import {DateService} from "../../services/date.service";
import {MatDateRangePicker} from "@angular/material/datepicker";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-housing-page',
  templateUrl: './housing-page.component.html',
  styleUrls: ['./housing-page.component.scss']
})
export class HousingPageComponent implements OnInit {
  @Input() housing: IHousing | null = null;

  nights: number = 0;

  private bookedDates: Date[] = []
  errorMessage: string = '';

  @ViewChild('picker') datePicker!: MatDateRangePicker<Date>;

  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });

  reservationForm = new FormGroup({
    additionalInfo: new FormControl<string>('')
  })

  constructor(private httpService: HttpService,
              private housingService: HousingService,
              private router: Router,
              private route: ActivatedRoute,
              private dateService: DateService
  ) {
  }

  ngOnInit(): void {
    if (!this.housing) {
      this.getHousing();
    } else {
      this.dateService.getBookedDays(this.housing.id).then(res => this.bookedDates = res);
    }
    this.trackDateValues();

    this.range.controls.start.setValue(this.dateService.checkInDate)
    this.range.controls.end.setValue(this.dateService.checkOutDate)
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

        this.nights = end.getDate().valueOf() - start.getDate().valueOf();
      }
    })
  }

  reserve() {
    let checkIn = this.range.controls.start.value;
    let checkOut = this.range.controls.end.value;
    if (this.housing && checkIn && checkOut) {
      let obj = {
        checkInDate: this.convertDate(checkIn),
        checkOutDate: this.convertDate(checkOut),
        additionalInfo: this.reservationForm.controls.additionalInfo.value
      }
      this.httpService.sendPostRequest(`/v1/housings/${this.housing.id}/book`, obj).subscribe(
        () => {
          this.router.navigate(['/bookings'])
        },
        error => {
          this.errorMessage = error.error;
        }
      )
    } else {
      this.errorMessage = "Choose dates";
    }
  }

  private getHousing(): void {
    this.route.paramMap.subscribe(async params => {
      const id = +params.get('id')!;
      await this.housingService.getHousingById(id)
        .then((housing) => {
          this.housing = housing;
          this.dateService.getBookedDays(housing.id).then(res => this.bookedDates = res);
        })
        .catch(() => this.router.navigate(['']));
    });
  }

  dateFilter = (d: Date | null): boolean => {
    if (!d) {
      d = new Date();
    }

    const currentDate = new Date();
    const yesterday = new Date(currentDate.getTime() - 24 * 60 * 60 * 1000);

    let alreadyBooked: boolean = false;
    if (d && this.bookedDates) {
      alreadyBooked = this.bookedDates.some(bookedDateString => {
        const bookedDate = new Date(bookedDateString);
        return (
          bookedDate.getDate() === d!.getDate() &&
          bookedDate.getMonth() === d!.getMonth() &&
          bookedDate.getFullYear() === d!.getFullYear()
        );
      });
    }

    return (d > new Date() || d > yesterday) && !alreadyBooked;
  };

  private convertDate(date: Date): string {
    return date.toLocaleDateString('en-GB', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
    });
  }
}
