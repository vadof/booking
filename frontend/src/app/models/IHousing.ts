import {ILocation} from "./ILocation";
import {IUser} from "./IUser";
import {IReview} from "./IReview";
import {IImage} from "./IImage";

export interface IHousing {
  id: number;
  name: string;
  location: ILocation;
  previewImage: IImage;
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
  images: IImage[];
}
