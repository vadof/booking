import {Component, Input, OnInit} from '@angular/core';
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HousingService} from "../../services/housing.service";

@Component({
  selector: 'app-housing-page',
  templateUrl: './housing-page.component.html',
  styleUrls: ['./housing-page.component.scss']
})
export class HousingPageComponent implements OnInit {
  @Input() housing: IHousing | null = null;

  constructor(private httpService: HttpService,
              private housingService: HousingService,
              private router: Router,
              private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
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
