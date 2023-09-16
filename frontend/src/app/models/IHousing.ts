import {IHousingDetails} from "./IHousingDetails";
import {ILocation} from "./ILocation";

export interface IHousing {
  id: number;
  name: string;
  location: ILocation;
  housingDetails: IHousingDetails | null;
  coordinates: string;
  pricePerNight: number;
  people: number;
  rating: number;
}
