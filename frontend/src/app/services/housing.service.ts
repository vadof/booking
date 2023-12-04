import {Injectable} from '@angular/core';
import {IHousing} from "../models/IHousing";
import {HttpService} from "./http.service";

@Injectable({
  providedIn: 'root'
})
export class HousingService {

  currentStep = 1;
  housing: IHousing | null = null;
  favouriteHousings: IHousing[] = []
  favouriteHousingsHasBeenRequested: boolean = false;

  constructor(private httpService: HttpService) {

  }

  async getHousingById(id: number): Promise<IHousing> {
    return await new Promise<IHousing>((resolve, reject) => {
      this.httpService.sendGetRequest(`/v1/housings/${id}`).subscribe(response => {
        resolve(response);
      }, error => {
        reject(error);
      })
    })
  }

  getFavourites(): Promise<IHousing[]> {
    return new Promise<IHousing[]>((resolve, reject) => {
      if (!this.favouriteHousingsHasBeenRequested) {
        this.httpService.sendGetRequest(`/v1/favourites`).subscribe(response => {
          this.favouriteHousingsHasBeenRequested = true;
          resolve(response);
          this.favouriteHousings = response;
        }, error => {
          reject(error);
        });
      } else {
        resolve(this.favouriteHousings);
      }
    });
  }

  removeFromFavourites(housing: IHousing) {
    this.favouriteHousings = this.favouriteHousings.filter((h) => h.id !== housing.id);
  }

  addToFavourites(housing: IHousing) {
    this.favouriteHousings.push(housing);
  }
}
