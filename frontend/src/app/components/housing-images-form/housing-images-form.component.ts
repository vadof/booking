import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {HousingService} from "../../services/housing.service";
import {HttpService} from "../../services/http.service";
import {TokenStorageService} from "../../auth/token-storage.service";
import {IImage} from "../../models/IImage";
import {API_URL} from "../../config/constants";

@Component({
  selector: 'app-housing-images-form',
  templateUrl: './housing-images-form.component.html',
  styleUrls: ['./housing-images-form.component.scss']
})
export class HousingImagesFormComponent implements OnInit {
  constructor(private httpClient: HttpClient,
              private housingService: HousingService,
              private httpService: HttpService,
              private tokenStorage: TokenStorageService) {
  }

  files: File[] = [];
  images: Image[] = [];
  selectedImage: Image | null = null;
  selectedPreviewImage: Image | null = null;
  selectedPreviewFile: File | null = null;
  loading: boolean = false;

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

        this.httpClient.post<IImage>(`${API_URL}/v1/images/${housingId}/upload`,
          uploadedImageData, {observe: 'response', headers}).subscribe(
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
    let pos1 = this.images.indexOf(image);
    let pos2 = pos1 === this.images.length - 1 ? 0 : pos1 + 1
    this.switchElementsInArray(this.images, pos1, pos2);
    this.switchElementsInArray(this.files, pos1, pos2);
  }

  dragImageToLeft(image: Image) {
    let pos1: number = this.images.indexOf(image);
    let pos2: number = pos1 !== 0 ? pos1 - 1 : this.images.length - 1;
    this.switchElementsInArray(this.images, pos1, pos2);
    this.switchElementsInArray(this.files, pos1, pos2);
  }

  private switchElementsInArray(arr: any[], pos1: number, pos2: number): void {
    let temp: any = arr[pos2];
    arr[pos2] = arr[pos1];
    arr[pos1] = temp;
  }

  back() {
    this.housingService.currentStep--;
  }
}

interface Image {
  file: File;
  imageSrc: string;
}
