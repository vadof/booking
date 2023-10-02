import {ILocation} from "./ILocation";
import {IUser} from "./IUser";
import {IReview} from "./IReview";

export interface IHousing {
  id: number;
  name: string;
  location: ILocation;
  imageSrc: string;
  coordinates: string;
  pricePerNight: number;
  people: number;
  rating: number;
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
