import { Injectable } from '@angular/core';
import { adInfo } from '../app/create-ad/ad-classes';
import { CITIES } from '../assets/data/cities';

@Injectable({
  providedIn: 'root'
})
export class AdCreationService {

  constructor() { }

  getCities() : string[] {
    //replace with GET request from DB
    return CITIES;
  }

  saveAd(ad : adInfo){
    //backend logic
  }
}
