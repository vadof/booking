import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-housing-delete-confirmation',
  templateUrl: './housing-delete-confirmation.component.html',
  styleUrls: ['./housing-delete-confirmation.component.scss']
})
export class HousingDeleteConfirmationComponent {
  constructor(
    public dialogRef: MatDialogRef<HousingDeleteConfirmationComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  onNoClick(): void {
    this.dialogRef.close(false);
  }

  onYesClick(): void {
    this.dialogRef.close(true);
  }
}
