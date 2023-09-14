import {IUser} from "./IUser";
import {IReview} from "./IReview";

export interface IHousingDetails {
  id: number;
  name: string;
  checkIn: string;
  checkOut: string;
  minAgeToRent: number;
  description: string;
  rooms: number;
  m2: number;
  minNights: number;
  owner: IUser;
  reviews: IReview[];
}
