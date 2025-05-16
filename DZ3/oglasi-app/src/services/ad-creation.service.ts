import { Injectable } from '@angular/core';
import { adInfo } from '../app/models/ad-classes';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AdService {

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

  getAdsByUser(userId : Number) : Observable<adInfo[]> {
    const url = `${this.baseUrl}/getAds/` + userId;
    return this.http.get<adInfo[]>(url);
  }

  getAdById(adId : number) : Observable<adInfo> {
    const url = `${this.baseUrl}/getAd/` + adId;
    return this.http.get<adInfo>(url);
  }
  updateAd(newAd: adInfo): Observable<adInfo>{
    const url = `${this.baseUrl}/updateAd`;
    const plainAd = {
      ...newAd,
      specs: Object.fromEntries(newAd.specs)
    };
    return this.http.patch<adInfo>(url, plainAd);
  }

  deleteAd(adId : number){
    const url = `${this.baseUrl}/deleteAd/` + adId;
    return this.http.delete<adInfo>(url);
  }
}
