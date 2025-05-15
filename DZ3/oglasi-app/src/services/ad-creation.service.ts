import { Injectable } from '@angular/core';
import { adInfo } from '../app/models/ad-classes';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AdCreationService {

  private baseUrl: string;

  constructor(private http: HttpClient) {
    this.baseUrl = 'http://localhost:8080';
  }

  getCities(): Observable<string[]> {
    const url = `${this.baseUrl}/cities`;
    return this.http.get<string[]>(url);
  }

  saveAd(ad : adInfo) : Observable<adInfo> {
    const url = `${this.baseUrl}/createAd`;
    const plainAd = {
      ...ad,
      specs: Object.fromEntries(ad.specs)
    };
    return this.http.post<adInfo>(url, plainAd);
  }
}
