export interface IHousingPaginatedResponse {
  page: number;
  totalPages: number;
  size: number;
  sortingFields: string;
  sortDirection: string;
  data: any
}
