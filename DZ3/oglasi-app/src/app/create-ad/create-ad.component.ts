import { Component, OnInit, TemplateRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators, FormsModule, AbstractControl } from "@angular/forms";
import { Router } from '@angular/router';
import { adInfo } from './ad-classes';
import { ReplaySubject, Subject } from 'rxjs';
import { startWith, map, takeUntil } from 'rxjs/operators';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { AdCreationService } from '../../services/ad-creation.service';

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
    MatIconModule,
    FormsModule
  ],
  templateUrl: './create-ad.component.html',
  styleUrl: './create-ad.component.css'
})
export class CreateAdComponent implements OnInit {
  adForm!: FormGroup;
  cities: string[] = [];
  filteredCities: ReplaySubject<string[]> = new ReplaySubject<string[]>(1);
  cityFilterCtrl: FormControl = new FormControl();
  private _onDestroy = new Subject<void>();
  specs = new Map<string, string>(
    [
      ['Color', 'Black'],
      ['Age', '2 years'],
      ['Breed', 'Siamese'],
      ['Weight', '4 kg'],
      ['Vaccinated', 'Yes']
    ]
  );
  pictures: string[] = [
    "https://upload.wikimedia.org/wikipedia/commons/4/4d/Cat_November_2010-1a.jpg",
    "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_16x9.jpg?w=1200",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTB6jQH76Aqx2EmQaDmxPhhwQWC3tys6xFyOg&s",
    "https://images.squarespace-cdn.com/content/v1/607f89e638219e13eee71b1e/1684821560422-SD5V37BAG28BURTLIXUQ/michael-sum-LEpfefQf4rU-unsplash.jpg",
    "https://icatcare.org/img/asset/aW1hZ2VzL2N1dG91dHMvYWRvYmVzdG9ja18zMTYzODM5NjkucG5n/adobestock_316383969.png?p=default&s=86bb641e805a56548c51f3385510026b",
    "https://cdn.britannica.com/39/226539-050-D21D7721/Portrait-of-a-cat-with-whiskers-visible.jpg"
  ];
  editSpecData = { key: '', value: '' };

  constructor( private router : Router, private dialog: MatDialog, private service : AdCreationService) { }

  ngOnInit() {
    this.cities = this.service.getCities();

    this.adForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.minLength(4)]),
      price: new FormControl('', [Validators.required, Validators.min(0)]),
      description: new FormControl(''),
      city: new FormControl('', [Validators.required]),
      type: new FormControl('', [Validators.required]),
      state: new FormControl('', [
        Validators.required,
        (control: AbstractControl) => {
          const type = this.adForm?.get('type')?.value;
          return control.value === 'both' && type !== 'buy'
            ? { invalidState: true }
            : null;
        }
      ]),
      specKey: new FormControl(''),
      specValue: new FormControl(''),
      image: new FormControl('')
    });
    this.filteredCities.next(this.cities.slice());

    this.cityFilterCtrl.valueChanges
      .pipe(
        takeUntil(this._onDestroy),
        startWith(''),
        map(search => search ? this.filterCities(search) : this.cities.slice())
      )
      .subscribe(filtered => this.filteredCities.next(filtered));

    this.adForm.get('type')?.valueChanges.subscribe(type => {
      const stateControl = this.adForm.get('state');
      stateControl?.enable();
      if (type !== 'buy' && stateControl?.value === 'both') {
        stateControl.setValue('');
      }
      stateControl?.updateValueAndValidity();
    });
  }

  private filterCities(search: string): string[] {
    const filterValue = search.toLowerCase();
    return this.cities.filter(city => city.toLowerCase().includes(filterValue));
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
    const createdAd = new adInfo(
      this.adForm.get('name')?.value,
      this.adForm.get('description')?.value,
      this.adForm.get('city')?.value,
      this.adForm.get('price')?.value,
      this.specs,
      this.pictures
    );
    this.service.saveAd(createdAd);
    //this.router.navigate(['/']);
  }

  openImageModal(imageUrl: string, templateRef: TemplateRef<any>): void {
    this.dialog.open(templateRef, {
      data: imageUrl,
      hasBackdrop: true,
      disableClose: false,
      maxWidth: '90vw',
      maxHeight: '90vh',
      width: 'auto',
      height: 'auto'
    });
  }

  openEditSpecModal(key: string, value: string, templateRef: TemplateRef<any>) {
    this.editSpecData = { key, value };
    this.dialog.open(templateRef, {
      data: this.editSpecData,
      panelClass: 'no-border-dialog',
      disableClose: false,
      height: '200px',
      width: '500px',
    })
  };

  openEditImageModal(url: string, index: number, dialogTemplate: TemplateRef<any>){
      this.dialog.open(dialogTemplate, {
        data: { url: url, index: index },
        panelClass: 'no-border-dialog',
        height: '400px',
        width: '800px',
    });
  };

  saveEditedSpec(data: { key: string, value: string }) {
    this.specs.set(data.key, data.value);
    this.dialog.closeAll();
  };

  saveEditedImage(data: { url: string; index: number }){
    if (data.url.trim()) {
      this.pictures[data.index] = data.url.trim();
      this.dialog.closeAll();
    }
  };

  removePicture(index: number){
    this.pictures.splice(index, 1);
    this.dialog.closeAll();
  }

  deleteSpec(key: string) {
    this.specs.delete(key);
    this.dialog.closeAll();
  };

  generateErrorForControl(control: AbstractControl){
    if(control.errors?.['required']){
      return "Required";
    }

    if(control.errors?.['min']){
      return "Minimum value is 0";
    }

    if(control.errors?.['minlength']){
      return "Must be at least 4 characters long!";
    }



    return '';
  }

}
