import { Component, OnInit, TemplateRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { adInfo } from '../models/ad-classes';
import { AdService } from '../../services/ad-creation.service';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators, FormsModule, AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";
import { Router } from '@angular/router';
import { ReplaySubject, Subject } from 'rxjs';
import { startWith, map, takeUntil } from 'rxjs/operators';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';


@Component({
  selector: 'app-edit-ad',
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
  templateUrl: './edit-ad.component.html',
  styleUrl: './edit-ad.component.css'
})
export class EditAdComponent implements OnInit {

  adId! : number;

  adForm!: FormGroup;
  cities: string[] = [];
  filteredCities: ReplaySubject<string[]> = new ReplaySubject<string[]>(1);
  cityFilterCtrl: FormControl = new FormControl();
  private _onDestroy = new Subject<void>();
  specs! : Map<string, string>;
  pictures!: string[];
  editSpecData = { key: '', value: '' };

  constructor(private adService : AdService, private route: ActivatedRoute, private dialog: MatDialog, private router: Router) {}

  ngOnInit(): void {
    this.adId = Number(this.route.snapshot.paramMap.get('id'));
    this.adService.getAdById(this.adId).subscribe({
      next: (ad : adInfo) => {
        this.adForm = new FormGroup({
          title: new FormControl(ad.title, [Validators.required, Validators.minLength(4)]),
          price: new FormControl(ad.price, [Validators.required, Validators.min(0)]),
          shortDesc: new FormControl(ad.shortDesc),
          longDesc: new FormControl(ad.longDesc),
          city: new FormControl(ad.city, [Validators.required]),
          type: new FormControl(ad.type, [Validators.required]),
          state: new FormControl(ad.state, [Validators.required]),
          specKey: new FormControl(''),
          specValue: new FormControl(''),
          image: new FormControl('')
        });
        this.specs = new Map<string, string>(Object.entries(ad.specs));
        this.pictures = ad.pictures;
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
    });
    this.adService.getCities().subscribe({
      next: (cities: string[]) => {
        this.cities = cities;
        this.filteredCities.next(cities);
      },
      error: (err) => {
        console.error("Error fetching cities:", err);
      }
    });

    this.cityFilterCtrl.valueChanges
      .pipe(
        takeUntil(this._onDestroy),
        startWith(''),
        map(search => search ? this.filterCities(search) : this.cities.slice())
      )
      .subscribe(filtered => this.filteredCities.next(filtered));
  }

  onSubmit(){
  }

  updateAd(){
    const updatedAd = new adInfo(
      this.adId,
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
    this.adService.updateAd(updatedAd).subscribe({
      next: () => {
        this.router.navigate(['/profile']);
      }
    });
  }

  deleteAd(){
    this.adService.deleteAd(this.adId).subscribe({
      next: () => {
        this.router.navigate(['/profile']);
      }
    });
  }

  ngOnDestroy() {
    this._onDestroy.next();
    this._onDestroy.complete();
  }

  private dynamicStateValidator(form: FormGroup): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const type = form.get('type')?.value;
      return control.value === 'both' && type !== 'buy'
        ? { invalidState: true }
        : null;
    };
  }

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
    return this.specs ?  Array.from(this.specs.entries()) : [];
  }

  removeSpec(key: string): void {
    this.specs.delete(key);
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
}
