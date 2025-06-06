import { Routes } from '@angular/router';
import { HomepageComponent } from './homepage/homepage.component';
import { CreateAdComponent } from './create-ad/create-ad.component';
import { ProfileComponent } from './profile/profile.component';
import { EditAdComponent } from './edit-ad/edit-ad.component';

export const routes: Routes = [
  {path: '', component: HomepageComponent},
  {path: 'create', component: CreateAdComponent},
  {path: 'profile', component: ProfileComponent},
  {path: 'editAd/:id', component: EditAdComponent},
  {path: '**', redirectTo: ''}, // Wildcard route for a pontential 404 page
];
