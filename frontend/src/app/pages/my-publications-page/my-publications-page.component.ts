import {Component, OnInit} from "@angular/core";
import {IHousing} from "../../models/IHousing";
import {HttpService} from "../../services/http.service";


@Component({
  selector: 'app-my-publications-page',
  templateUrl: './my-publications-page.component.html',
  styleUrls: ['./my-publications-page.component.scss']
})

export class MyPublicationsPageComponent implements OnInit {

  housings: IHousing[] = [];

  constructor(
    private httpService: HttpService
  ) {
  }

  ngOnInit() {
    this.httpService.sendGetRequest('/v1/housings/my').subscribe(
      response => {
        this.housings = response as IHousing[];
      }
    )
  }
}
