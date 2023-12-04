import {Component, Input, OnInit} from '@angular/core';
import {IReview} from "../../models/IReview";

@Component({
  selector: 'app-housing-review-item',
  templateUrl: './housing-review-item.component.html',
  styleUrls: ['./housing-review-item.component.scss']
})
export class HousingReviewItemComponent implements OnInit {
  @Input() review!: IReview;

  ngOnInit(): void {
  }
}
