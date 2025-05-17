import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatButtonModule } from '@angular/material/button';
import { adInfo } from '../models/ad-classes';
import { AdService } from '../../services/ad-creation.service';
import { Router } from '@angular/router';
import { MatInputModule } from '@angular/material/input';
@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    MatTabsModule,
    MatIconModule,
    CommonModule,
    MatButtonModule,
    MatInputModule,
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {

  userId : number = 1;
  myAds : adInfo[] = [];
  filteredAds : adInfo[] = [];

  constructor(private adService : AdService, private router: Router) { }

  ngOnInit(): void {
    this.adService.getAdsByUser(this.userId).subscribe({
      next: (ads : adInfo[]) => {
        this.myAds = ads;
        this.filteredAds = ads;
      }
    })

  }

  editAd(adId : number){
    this.router.navigate(['/editAd', adId]);
  }

  filterAds(searchQuery : string){
    const lowerQuery = searchQuery.toLowerCase().trim();
    this.filteredAds = this.myAds.filter(ad =>
      ad.title.toLowerCase().includes(lowerQuery) ||
      ad.shortDesc.toLowerCase().includes(lowerQuery)
    );
  }
}
