import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { MainPageComponent } from './pages/main-page/main-page.component';
import { RegisterPageComponent } from './pages/register-page/register-page.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HousingPreviewItemComponent } from './components/housing-preview-item/housing-preview-item.component';
import { LocationPipe } from './pipes/location.pipe';
import {MatInputModule} from "@angular/material/input";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {MatFormFieldModule} from "@angular/material/form-field";
import {provideAnimations} from "@angular/platform-browser/animations";
import { HousingAddPageComponent } from './pages/housing-add-page/housing-add-page.component';
import { HousingFormComponent } from './components/housing-form/housing-form.component';
import { HousingImagesFormComponent } from './components/housing-images-form/housing-images-form.component';
import {NgxDropzoneModule} from "ngx-dropzone";
import { HousingPageComponent } from './pages/housing-page/housing-page.component';
import { HousingItemComponent } from './components/housing-item/housing-item.component';
import { MyPublicationsPageComponent } from './pages/my-publications-page/my-publications-page.component';
import { HousingReviewItemComponent } from './components/housing-review-item/housing-review-item.component';
import { HousingPublishFormComponent } from './components/housing-publish-form/housing-publish-form.component';
import {MatSliderModule} from "@angular/material/slider";
import { BookingHistoryPageComponent } from './pages/booking-history-page/booking-history-page.component';
import {BookingItemComponent} from "./components/booking-item/booking-item.component";
import { ReviewDialogComponent } from './review-dialog/review-dialog.component';



@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    MainPageComponent,
    RegisterPageComponent,
    NavbarComponent,
    HousingPreviewItemComponent,
    LocationPipe,
    HousingAddPageComponent,
    HousingFormComponent,
    HousingImagesFormComponent,
    HousingPageComponent,
    HousingItemComponent,
    BookingHistoryPageComponent,
    BookingItemComponent,
    ReviewDialogComponent,
    MyPublicationsPageComponent,
    HousingReviewItemComponent,
    HousingPublishFormComponent,
    HousingPreviewDeleteItemComponent,
    HousingDeleteConfirmationComponent,
  ],
    imports: [
        BrowserModule,
        HttpClientModule,
        AppRoutingModule,
        ReactiveFormsModule,
        NgbModule,
        FormsModule,

    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    NgxDropzoneModule,
    NgOptimizedImage,
    MatDialogModule,
    MatButtonModule,
    MatSliderModule,
  ],
  providers: [
    provideAnimations()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
