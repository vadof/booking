import {IUser} from "./IUser";
import {IHousing} from "./IHousing";

export interface IBooking {
  id: number;
  tenant: IUser;
  housing: IHousing;
  checkInDate: string;
  checkOutDate: string;
  nights: number;
  additionalInfo: string;
}
