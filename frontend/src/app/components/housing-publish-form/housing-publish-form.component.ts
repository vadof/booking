import {Component, OnInit} from '@angular/core';
import {HousingService} from "../../services/housing.service";
import {HttpService} from "../../services/http.service";
import {Router} from "@angular/router";
import {IHousing} from "../../models/IHousing";

@Component({
  selector: 'app-housing-publish-form',
  templateUrl: './housing-publish-form.component.html',
  styleUrls: ['./housing-publish-form.component.scss']
})
export class HousingPublishFormComponent implements OnInit {

  housing: IHousing | null = null;

  constructor(private housingService: HousingService,
              private httpService: HttpService,
              private router: Router) {
  }

  ngOnInit(): void {
    const housing = this.housingService.housing;
    if (housing) {
      this.httpService.sendGetRequest(`/v1/housings/${housing.id}`)
        .subscribe(res => this.housing = res as IHousing)
    }
  }

  back() {
    this.housingService.currentStep--;
  }

  publish() {
    if (this.housing) {
      this.httpService.sendPutRequest(`/v1/housings/publish/${this.housing.id}?value=true`, null).subscribe(
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
