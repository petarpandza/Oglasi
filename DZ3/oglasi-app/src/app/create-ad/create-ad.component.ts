import { Component, OnInit, TemplateRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators, FormsModule, AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";
import { adInfo } from '../models/ad-classes';
import { Subject } from 'rxjs';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { AdService } from '../../services/ad-creation.service';

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
  showSuccessMessage: boolean = false;
  adForm!: FormGroup;
  cities: string[] = [];
  filteredCities: string[] = [];
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

  constructor( private dialog: MatDialog, private adService : AdService) { }

  ngOnInit() {
    this.adService.getCities().subscribe({
      next: (cities: string[]) => {
        this.cities = cities;
        this.filteredCities = cities
      },
      error: (err) => {
        console.error("Error fetching cities:", err);
      }
    });

    this.adForm = new FormGroup({
      title: new FormControl('', [Validators.required, Validators.minLength(4)]),
      price: new FormControl('', [Validators.required, Validators.min(0)]),
      shortDesc: new FormControl(''),
      longDesc: new FormControl(''),
      city: new FormControl('', [Validators.required]),
      type: new FormControl('', [Validators.required]),
      state: new FormControl('', [Validators.required]),
      specKey: new FormControl(''),
      specValue: new FormControl(''),
      image: new FormControl('')
    });
    this.adForm.get('state')?.addValidators(this.dynamicStateValidator(this.adForm));

    this.adForm.get('type')?.valueChanges.subscribe(type => {
      const stateControl = this.adForm.get('state');
      stateControl?.enable();
      if (type !== 'buy' && stateControl?.value === 'both') {
        stateControl.setValue('');
      }
      stateControl?.updateValueAndValidity();
    });
  }

  filterCities(searchQuery : string){
    const lowerQuery = searchQuery.toLowerCase().trim();
    this.filteredCities = this.cities.filter(city =>
      city.toLowerCase().includes(lowerQuery)
    );
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
      0,
      this.adForm.get('title')?.value,
      this.adForm.get('shortDesc')?.value,
      this.adForm.get('longDesc')?.value,
      this.adForm.get('city')?.value,
      this.adForm.get('type')?.value,
      this.adForm.get('state')?.value,
      this.adForm.get('price')?.value,
      this.specs,
      this.pictures
    );
    this.adService.saveAd(createdAd).subscribe({
      next : (response => {
        if(response){
          this.showSuccessMessage = true
          setTimeout(()=>{
            this.showSuccessMessage = false;
            this.adForm.reset();
            this.specs.clear();
            this.pictures.length = 0;
          }, 3000)
        }
      })
    });
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
        disableClose: false,
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

  private dynamicStateValidator(form: FormGroup): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const type = form.get('type')?.value;
      return control.value === 'both' && type !== 'buy'
        ? { invalidState: true }
        : null;
    };
  }

}

