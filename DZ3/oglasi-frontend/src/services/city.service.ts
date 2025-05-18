import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CityService {

  private baseUrl: string;

  constructor(private http: HttpClient) {
    this.baseUrl = 'http://localhost:8080';
  }

  getCities(): Observable<string[]> {
      const url = `${this.baseUrl}/cities`;
      return this.http.get<string[]>(url);
    }
}
