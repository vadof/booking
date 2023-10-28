import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HousingService} from "../../services/housing.service";
import {MatDateRangePicker} from "@angular/material/datepicker";
import {FormControl, FormGroup} from "@angular/forms";
import {IReview} from "../../models/IReview";
import {IHousingPaginatedResponse} from "../../reponses/IHousingPaginatedResponse";

@Component({
  selector: 'app-housing-page',
  templateUrl: './housing-page.component.html',
  styleUrls: ['./housing-page.component.scss']
})
export class HousingPageComponent implements OnInit {
  @Input() housing: IHousing | null = null;
  //@Input() review: IReview | null = null;

  currentPage: number = 0;
  totalPages: number = 0;
  contentSize: number = 10;

  pages: number[] = []

  reviews: IReview[] = [];

  @ViewChild('picker') datePicker!: MatDateRangePicker<Date>;

  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });

  constructor(private httpService: HttpService,
              private housingService: HousingService,
              private router: Router,
              private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    if (!this.housing) {
      this.getHousing();
      this.searchReviews();
    }
  }

  private getHousing(): void {
    this.route.paramMap.subscribe(async params => {
      const id = +params.get('id')!;
      await this.housingService.getHousingById(id)
        .then((housing) => this.housing = housing)
        .catch(() => this.router.navigate(['']));
    });
  }

  searchReviews() {
    this.reviews = this.housing.reviews
    let path = '/v1/reviews?';
    let params: string[] = [];
    params.push(`page=${this.currentPage}`)
    params.push(`size=${this.contentSize}`)
    params.push(`housing=${this.housing}`)

    for (let i = 0; i < params.length; i++) {
      path += params[i];
      if (i !== params.length - 1) path += '&';
    }


    this.httpService.sendGetRequest(path).subscribe(
      response => {
        const paginatedResponse: IHousingPaginatedResponse = response as IHousingPaginatedResponse;
        this.totalPages = paginatedResponse.totalPages;
        this.reviews = paginatedResponse.data;
        this.pages = Array.from({length: this.totalPages}, (_, i) => i);
      }
    )
  }

  changePage(page: number) {
    this.currentPage = page;
    this.searchReviews();
  }
}
