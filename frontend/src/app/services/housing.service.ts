import { Injectable } from '@angular/core';
import {IHousing} from "../models/IHousing";

@Injectable({
  providedIn: 'root'
})
export class HousingService {

  currentStep = 1;
  housing: IHousing | null = null;

  constructor() {

  }
}
