import {Component, Input, OnInit} from '@angular/core';
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";
import {HousingService} from "../../services/housing.service";

@Component({
  selector: 'app-housing-preview-item',
  templateUrl: './housing-preview-item.component.html',
  styleUrls: ['./housing-preview-item.component.scss']
})
export class HousingPreviewItemComponent implements OnInit {
  @Input() housing!: IHousing;
  @Input() nights: number = 1;
  totalPrice: number = 0;
  isFavourite: boolean = false;
  housings: IHousing[] = [];
  errorMessage: string = '';

  constructor(private httpService: HttpService, private housingService: HousingService) {
  }

  ngOnInit(): void {
    this.totalPrice = Math.round(this.housing.pricePerNight * this.nights * 100) / 100;
    this.housingService.getFavourites().then(res => {
      this.isFavourite = res.some(h => h.id === this.housing.id)
    });
  }

  addToFavourites(housing: IHousing) {
    if (housing) {
      this.httpService.sendPostRequest(`/v1/favourites/${this.housing.id}`, null).subscribe(
        response => {
          this.isFavourite = true;
          this.housingService.addToFavourites(response as IHousing);
        }, err => {
          console.log(err);
        }
      )
    }
  }

  removeFromFavourites() {
    this.httpService.sendDeleteRequest(`/v1/favourites/${this.housing.id}`).subscribe(
      response => {
        this.isFavourite = false;
        this.housingService.removeFromFavourites(response as IHousing);
      },
      error => {
        console.error('Error deleting housing:', error);
        this.errorMessage = error.error;
      }
    );
  }

  toggleFavorite(): void {
    if (this.isFavourite) {
      const confirmRemove = window.confirm('Are you sure you want to remove this item from favorites?');
      if (confirmRemove) {
        this.removeFromFavourites()
      }
    } else {
      this.addToFavourites(this.housing)
    }
  }
}
