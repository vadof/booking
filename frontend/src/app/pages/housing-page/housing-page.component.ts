import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HousingService} from "../../services/housing.service";
import {MatDateRangePicker} from "@angular/material/datepicker";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-housing-page',
  templateUrl: './housing-page.component.html',
  styleUrls: ['./housing-page.component.scss']
})
export class HousingPageComponent implements OnInit {
  @Input() housing: IHousing | null = null;

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
    this.housing = {
      id: 1,
      name: 'Tallinn Rich Hotel',
      location: {id: 1, name: 'Tallinn'},
      previewImage: {id: 1, imageSrc: 'http://localhost:8080/api/v1/images/30'},
      coordinates: '55.3232,27.3232',
      pricePerNight: 54.74,
      people: 2,
      rating: 10,
      checkIn: '14:00',
      checkOut: '12:00',
      minAgeToRent: 18,
      description: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab alias aliquam at culpa dolore ' +
        'ea fugit illo ipsa laborum laudantium minus nesciunt optio quam quas qui, quia quos tenetur velit.',
      rooms: 1,
      m2: 23,
      minNights: 1,
      reviews: [],
      images: [{id: 97, imageSrc: 'http://localhost:8080/api/v1/images/97'},
        {id: 98, imageSrc: 'http://localhost:8080/api/v1/images/98'},
        {id: 99, imageSrc: 'http://localhost:8080/api/v1/images/99'},
        {id: 100, imageSrc: 'http://localhost:8080/api/v1/images/100'},
        {id: 101, imageSrc: 'http://localhost:8080/api/v1/images/101'},
        {id: 102, imageSrc: 'http://localhost:8080/api/v1/images/102'},
        {id: 103, imageSrc: 'http://localhost:8080/api/v1/images/103'},
        {id: 104, imageSrc: 'http://localhost:8080/api/v1/images/104'},
        {id: 105, imageSrc: 'http://localhost:8080/api/v1/images/105'},
        {id: 106, imageSrc: 'http://localhost:8080/api/v1/images/106'},
        {id: 107, imageSrc: 'http://localhost:8080/api/v1/images/107'},
        {id: 108, imageSrc: 'http://localhost:8080/api/v1/images/108'},
        {id: 109, imageSrc: 'http://localhost:8080/api/v1/images/109'}]
    }

    if (!this.housing) {
      this.getHousing();
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
}
