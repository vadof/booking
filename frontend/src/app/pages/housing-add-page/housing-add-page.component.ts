import {Component, OnInit} from '@angular/core';
import {HousingService} from "../../services/housing.service";

@Component({
  selector: 'app-housing-add-page',
  templateUrl: './housing-add-page.component.html',
  styleUrls: ['./housing-add-page.component.scss']
})
export class HousingAddPageComponent implements OnInit {

  constructor(private housingService: HousingService) {
  }

  ngOnInit(): void {

  }

  getCurrentStep(): number {
    return this.housingService.currentStep;
  }

}
