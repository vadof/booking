import {IUser} from "./IUser";

export interface IReview {
  id: number;
  text: string;
  rating: number;
  reviewer: IUser;
}
