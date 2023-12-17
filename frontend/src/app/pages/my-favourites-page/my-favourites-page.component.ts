import {Component} from '@angular/core';
import {IHousing} from "../../models/IHousing";
import {HousingService} from "../../services/housing.service";

@Component({
  selector: 'app-my-favourites-page',
  templateUrl: './my-favourites-page.component.html',
  styleUrls: ['./my-favourites-page.component.scss']
})
export class MyFavouritesPageComponent {
  housings: IHousing[] = [];

  constructor(
    private housingService: HousingService,
  ) {
  }

  ngOnInit() {
    this.housingService.getFavourites().then(res => this.housings = res);
  }
}

