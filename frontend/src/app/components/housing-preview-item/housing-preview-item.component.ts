import {Component, Input, OnInit} from '@angular/core';
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";
import {HousingService} from "../../services/housing.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {coordinatesValidator} from "../../validators/Coordinates";
import {positiveNumberValidator} from "../../validators/PositiveNumber";
import {checkInCheckOutValidator} from "../../validators/CheckIn";

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


  constructor(private httpService: HttpService, private housingService: HousingService) { }

  ngOnInit(): void {
    this.totalPrice = Math.round(this.housing.pricePerNight * this.nights * 100) / 100;
    this.getFavourites().subscribe(favoriteHousings => {
      this.isFavourite = favoriteHousings.some((favHousing: { id: number; }) => favHousing.id === this.housing.id);
    });
  }

  addToFavourites(housingInput: IHousing) {
    if (housingInput) {
      this.housing = housingInput;
      this.httpService.sendPostRequest(`/v1/favourites/${this.housing.id}`, null).subscribe(
        response => {
          this.housingService.housing = response as IHousing;
        }, err => {
          console.log(err);
        }
      )
    }
  }

  getFavourites() {
    return this.httpService.sendGetRequest('/v1/favourites');
  }

  removeFromFavourites() {
    this.httpService.sendDeleteRequest(`/v1/favourites/${this.housing.id}`).subscribe(
      response => {
        console.log("Deleted successfully");
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
        this.isFavourite = false;
      }
    } else {
      this.addToFavourites(this.housing)
      this.isFavourite = true
    }
  }
}
