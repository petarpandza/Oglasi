import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators, FormBuilder } from "@angular/forms";
import { Router } from '@angular/router';
import { adInfo } from './ad-classes';
import { CITIES } from '../../assets/data/cities';
import { ReplaySubject, Subject } from 'rxjs';
import { startWith, map, takeUntil } from 'rxjs/operators';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';

import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';

@Component({
  selector: 'app-create-ad',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    MatSelectModule,
    MatInputModule,
    NgxMatSelectSearchModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './create-ad.component.html',
  styleUrl: './create-ad.component.css'
})
export class CreateAdComponent implements OnInit {
  adForm!: FormGroup;
  cities: string[] = CITIES;
  filteredCities: ReplaySubject<string[]> = new ReplaySubject<string[]>(1);
  cityFilterCtrl: FormControl = new FormControl();
  private _onDestroy = new Subject<void>();
  specs = new Map<string, string>([
        ["key1", "value1"],
        ["key2", "value2"]
    ]);
  pictures: string[] = [
    "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_16x9.jpg?w=1200",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6d87zy2l97Gbuz1xheO71Fzw31vhLFurSyg&s"
  ];

  constructor( private router : Router, private fb: FormBuilder, private dialog: MatDialog) { }

  ngOnInit() {
    this.adForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(4)]],
      description: [''],
      city: ['', Validators.required],
      price: ['', [Validators.required]],
      specKey: [''],
      specValue: [''],
      image: [''],
      type: ['', Validators.required],
      state: ['', Validators.required]
    });

    this.filteredCities.next(this.cities.slice());

    this.cityFilterCtrl.valueChanges
      .pipe(
        startWith(''),
        takeUntil(this._onDestroy),
        map(search => search ? this.filterCities(search) : this.cities.slice())
      )
      .subscribe(filtered => this.filteredCities.next(filtered));
  }

  private filterCities(search: string): string[] {
    const filterValue = search.toLowerCase();
    return this.cities.filter(city => city.toLowerCase().includes(filterValue));
  }

  removePicture(key: string){
    const index = this.pictures.indexOf(key);
    this.pictures.splice(index, 1);
  }

  addPicture(){
    const picture = this.adForm.get('image')?.value?.trim();
    if (picture) {
      this.pictures.push(picture);
      this.adForm.get('image')?.setValue('');
    }
  }

  addSpec(){
    const key = this.adForm.get('specKey')?.value?.trim();
    const value = this.adForm.get('specValue')?.value?.trim();
    if (key && value) {
      this.specs.set(key, value);
      this.adForm.get('specKey')?.setValue('');
      this.adForm.get('specValue')?.setValue('');
    }
  }

  get specsEntries() {
    return Array.from(this.specs.entries());
  }

  removeSpec(key: string): void {
    this.specs.delete(key);
  }

  ngOnDestroy() {
    this._onDestroy.next();
    this._onDestroy.complete();
  }

  onSubmit(){
    this.router.navigate(['/']);
  }

  openImageModal(imageUrl: string, templateRef: TemplateRef<any>): void {
    this.dialog.open(templateRef, {
      data: imageUrl,
      panelClass: 'custom-dialog-container',
      hasBackdrop: true,
      disableClose: false,
      maxWidth: '90vw',
      maxHeight: '90vh',
      width: 'auto',
      height: 'auto'
    });
  }
}
