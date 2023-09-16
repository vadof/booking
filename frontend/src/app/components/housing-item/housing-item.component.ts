import {Component, Input} from '@angular/core';
import {IHousing} from "../../models/IHousing";

@Component({
  selector: 'app-housing-item',
  templateUrl: './housing-item.component.html',
  styleUrls: ['./housing-item.component.scss']
})
export class HousingItemComponent {
  @Input() housing!: IHousing;
}
