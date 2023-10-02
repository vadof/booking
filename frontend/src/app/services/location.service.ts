import {Injectable} from '@angular/core';
import {HttpService} from "./http.service";
import {ILocation} from "../models/ILocation";

@Injectable({
  providedIn: 'root'
})
export class LocationService {

  private locations: ILocation[] = [];

  constructor(private http: HttpService) {
  }

  public getLocations(): Promise<ILocation[]> {
    return new Promise<ILocation[]>((resolve, reject) => {
      if (this.locations.length === 0) {
        this.http.sendGetRequest('/v1/locations').subscribe(
          response => {
            resolve(response as ILocation[]);
          },
          error => {
            reject(error.error);
          }
        );
      } else {
        resolve(this.locations);
      }
    });
  }
}
