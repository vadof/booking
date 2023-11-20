import {Component, Input} from '@angular/core';
import {IHousing} from "../../models/IHousing";
import { MatDialog } from '@angular/material/dialog';
import {HousingDeleteConfirmationComponent} from "../housing-delete-confirmation/housing-delete-confirmation.component";
import {HttpService} from "../../services/http.service";

@Component({
  selector: 'app-housing-preview-delete-item',
  templateUrl: './housing-preview-delete-item.component.html',
  styleUrls: ['./housing-preview-delete-item.component.scss']
})
export class HousingPreviewDeleteItemComponent {
  @Input() housing!: IHousing;
  @Input() nights: number = 1;
  totalPrice: number = 0;
  imageUrl = 'assets/delete.png';
  errorMessage: string = '';


  ngOnInit(): void {
    this.totalPrice = Math.round(this.housing.pricePerNight * this.nights * 100) / 100;
  }

  constructor(public dialog: MatDialog, private httpService: HttpService) { }

  openDeleteConfirmationDialog(): void {
    const dialogRef = this.dialog.open(HousingDeleteConfirmationComponent, {
      width: '300px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteItem();
      }
    });
  }

  deleteItem() {
    this.httpService.sendDeleteRequest(`/v1/housings/${this.housing.id}`).subscribe(
      response => {
        console.log("Deleted successfully");
      },
      error => {
        console.error('Error deleting housing:', error);
        this.errorMessage = error.error;
      }
    );
  }
}
