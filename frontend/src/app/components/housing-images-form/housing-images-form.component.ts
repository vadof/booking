import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {HousingService} from "../../services/housing.service";
import {HttpService} from "../../services/http.service";
import {TokenStorageService} from "../../auth/token-storage.service";
import {IImage} from "../../models/IImage";

@Component({
  selector: 'app-housing-images-form',
  templateUrl: './housing-images-form.component.html',
  styleUrls: ['./housing-images-form.component.scss']
})
export class HousingImagesFormComponent implements OnInit {
  constructor(private httpClient: HttpClient,
              private housingService: HousingService,
              private httpService: HttpService,
              private tokenStorage: TokenStorageService) { }

  files: File[] = [];
  images: Image[] = [];
  selectedImage: Image | null = null;
  selectedPreviewImage: Image | null = null;
  selectedPreviewFile: File | null = null;
  loading: boolean = false;

  // TODO accept only images
  onSelect(event: any) {
    this.files.push(...event.addedFiles);
    this.images = [];
    this.files.forEach(f => {
      this.images.push({file: f, imageSrc: this.getSafeURL(f)});
    })

    this.selectedImage = null;
  }

  removeFile(index: number) {
    this.files.splice(index, 1);
    this.images.splice(index, 1);
  }

  getSafeURL(file: File): any {
    return window.URL.createObjectURL(file);
  }

  selectPreviewImage() {
    this.selectedPreviewImage = this.selectedImage;
    let index = this.images.indexOf(this.selectedPreviewImage!);
    this.selectedPreviewFile = this.files[index];
  }

  async uploadFiles() {
    if (this.housingService.housing) {
      const housingId = this.housingService.housing.id;
      this.loading = true;
      const images: IImage[] = [];
      const headers = new HttpHeaders({
        'Authorization': 'Bearer ' + this.tokenStorage.getToken()!
      });

      await this.files.forEach(file => {
        let uploadedImageData = new FormData()
        uploadedImageData.append('imageFile', file);

        this.httpClient.post<IImage>(`http://localhost:8080/api/v1/images/${housingId}/upload`,
          uploadedImageData, { observe: 'response', headers }).subscribe(
            response => {
              images.push(response.body!);
              if (file === this.selectedPreviewFile) {
                this.setPreviewImage(response.body!.id, housingId)
              }
            }, error => {
            console.log(error)
          })
      })

      this.housingService.housing!.images = images;
      this.housingService.currentStep++;
    }
  }

  private setPreviewImage(imageId: number, housingId: number) {
    this.httpService.sendPutRequest(`/v1/housings/${housingId}/previewImage/${imageId}`, null).subscribe(
      response => {
        this.housingService.housing!.previewImage = response.body!;
      }, error => {
        console.error(error);
      }
    );
  }

  ngOnInit(): void {

  }

  dragImageToRight(image: Image) {
    let index = this.images.indexOf(image);

    if (index === this.images.length - 1) {
      let temp = this.images[0];
      this.images[0] = this.images[index];
      this.images[index] = temp;

      let temp1 = this.files[0];
      this.files[0] = this.files[index];
      this.files[index] = temp1;
    } else {
      let temp = this.images[index + 1]
      this.images[index + 1] = this.images[index];
      this.images[index] = temp;

      let temp1 = this.files[index + 1]
      this.files[index + 1] = this.files[index];
      this.files[index] = temp1;
    }
  }

  dragImageToLeft(image: Image) {
    let index = this.images.indexOf(image);

    if (index === 0) {
      let temp = this.images[this.images.length - 1];
      this.images[this.images.length - 1] = this.images[0];
      this.images[0] = temp;

      let temp1 = this.files[this.images.length - 1];
      this.files[this.images.length - 1] = this.files[0];
      this.files[0] = temp1;
    } else {
      let temp = this.images[index - 1]
      this.images[index - 1] = this.images[index];
      this.images[index] = temp;

      let temp1 = this.files[index - 1];
      this.files[index - 1] = this.files[index];
      this.files[index] = temp1;
    }
  }
}

interface Image {
  file: File;
  imageSrc: string;
}
