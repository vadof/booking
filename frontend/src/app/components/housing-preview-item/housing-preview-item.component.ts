import {Component, Input, OnInit} from '@angular/core';
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";

@Component({
  selector: 'app-housing-preview-item',
  templateUrl: './housing-preview-item.component.html',
  styleUrls: ['./housing-preview-item.component.scss']
})
export class HousingPreviewItemComponent implements OnInit {
  @Input() housing!: IHousing;
  @Input() nights: number = 1;
  totalPrice: number = 0;

  constructor(
    private httpService: HttpService
  ) {
  }

  ngOnInit(): void {
    this.totalPrice = Math.round(this.housing.pricePerNight * this.nights * 100) / 100;
  }

  addToFavourites() {
    if (this.housing) {
      this.httpService.sendPostRequest(`/v1/favourites`, null).subscribe(
        response => {
          this.housing = response as IHousing;
          this.router.navigate([`housing/${this.housing.id}`]).then(
            () => {
              this.housingService.currentStep = 1;
              this.housingService.housing = null;
            }
          );
        }, err => {
          console.log(err);
        }
      )
    }
  }
}
