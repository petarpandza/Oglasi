import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { City } from '../app/models/city';
import { OglasiConstants } from '../oglasi-constants';

@Injectable({
  providedIn: 'root'
})
export class CityService {

  private baseUrl: string;

  constructor(private http: HttpClient) {
    this.baseUrl = OglasiConstants.BASE_URL + '/city';
  }

  getCities(): Observable<City[]> {
      const url = `${this.baseUrl}/getCities`;
      return this.http.get<City[]>(url);
    }
}
