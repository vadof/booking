import {IUser} from "./IUser";

export interface IReviewDTO {
  id: number;
  text: string;
  rating: number;
  date: Date;
  reviewer: IUser;
}
