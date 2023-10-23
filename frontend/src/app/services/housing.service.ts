import { Injectable } from '@angular/core';
import {IHousing} from "../models/IHousing";
import {HttpService} from "./http.service";

@Injectable({
  providedIn: 'root'
})
export class HousingService {

  currentStep = 1;
  housing: IHousing | null = null;

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
}
