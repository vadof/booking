import {Injectable} from '@angular/core';
import {HttpService} from "./http.service";

@Injectable({
  providedIn: 'root'
})
export class DateService {

  checkInDate: Date | null = null;
  checkOutDate: Date | null = null;

  constructor(private httpService: HttpService) {
  }

  getBookedDays(housingId: number): Promise<Date[]> {
    return new Promise<Date[]>((resolve, reject) => {
      this.httpService.sendGetRequest(`/v1/housings/${housingId}/booked-dates`).subscribe(response => {
        resolve(response);
      }, error => {
        reject(error);
      });
    });
  }
}
