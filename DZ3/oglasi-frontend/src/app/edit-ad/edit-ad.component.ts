import { Component, OnInit, TemplateRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { adInfo } from '../models/ad-classes';
import { AdService } from '../../services/ad.service';
import { CommonModule } from '@angular/common';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
  FormsModule,
  AbstractControl,
  ValidationErrors,
  ValidatorFn,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { CityService } from '../../services/city.service';
import { City } from '../models/city';
import { OglasiConstants } from '../../oglasi-constants';

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
    FormsModule,
  ],
  templateUrl: './edit-ad.component.html',
  styleUrl: './edit-ad.component.css',
})
export class EditAdComponent implements OnInit {
  adId!: number;

  adForm!: FormGroup;
  cities: City[] = [];
  filteredCities: City[] = [];
  private _onDestroy = new Subject<void>();
  specs!: Map<string, string>;
  filteredSpecs!: Map<string, string>;
  pictures!: string[];
  editSpecData = { key: '', value: '' };

  constructor(
    private adService: AdService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private router: Router,
    private cityService: CityService
  ) {}

  ngOnInit(): void {
    this.adId = Number(this.route.snapshot.paramMap.get('id'));
    this.cityService.getCities().subscribe({
      next: (cities: City[]) => {
        this.cities = cities;
        this.filteredCities = cities;

        this.adService.getAdById(this.adId).subscribe({
          next: (ad: adInfo) => {
            const matchedCity = this.cities.find(
              (city) => city.id_city === ad.city.id_city
            );
            this.adForm = new FormGroup({
              title: new FormControl(ad.title, [
                Validators.required,
                Validators.minLength(4),
              ]),
              price: new FormControl(ad.price, [
                Validators.required,
                Validators.min(0),
              ]),
              shortDesc: new FormControl(ad.shortDesc),
              longDesc: new FormControl(ad.longDesc),
              city: new FormControl(matchedCity, [Validators.required]),
              type: new FormControl(ad.type, [Validators.min(1)]),
              state: new FormControl(ad.state, [Validators.min(1)]),
              specKey: new FormControl(''),
              specValue: new FormControl(''),
              image: new FormControl(''),
            });
            this.specs = new Map<string, string>(Object.entries(ad.specs));
            this.filteredSpecs = this.specs;
            this.pictures = ad.pictures;
            this.adForm
              .get('state')
              ?.addValidators(this.dynamicStateValidator(this.adForm));
            this.adForm.get('type')?.valueChanges.subscribe((type) => {
              const stateControl = this.adForm.get('state');
              stateControl?.enable();
              if (
                type !== OglasiConstants.TYPE_BUY &&
                +stateControl?.value === OglasiConstants.STATE_BOTH
              ) {
                stateControl?.setValue(0);
              }
              stateControl?.updateValueAndValidity();
            });
          },
        });
      },
      error: (err) => {
        console.error('Error fetching cities:', err);
      },
    });
  }

  onSubmit() {}

  filterCities(searchQuery: string) {
    const lowerQuery = searchQuery.toLowerCase().trim();
    this.filteredCities = this.cities.filter((city) =>
      city.name.toLowerCase().includes(lowerQuery)
    );
  }

  filterSpecs(searchQuery: string) {
    const lowerQuery = searchQuery.toLowerCase().trim();
    this.filteredSpecs = new Map(
      Array.from(this.specs.entries()).filter(([key, value]) =>
        key.toLowerCase().includes(lowerQuery)
      )
    );
  }

  updateAd() {
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
      },
    });
  }

  deleteAd() {
    this.adService.deleteAd(this.adId).subscribe({
      next: () => {
        this.router.navigate(['/profile']);
      },
    });
  }

  ngOnDestroy() {
    this._onDestroy.next();
    this._onDestroy.complete();
  }

  private dynamicStateValidator(form: FormGroup): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const type = form.get('type')?.value;
      return control.value === OglasiConstants.STATE_BOTH &&
        type !== OglasiConstants.TYPE_BUY
        ? { invalidState: true }
        : null;
    };
  }

  generateErrorForControl(control: AbstractControl) {
    if (control.errors?.['required']) {
      return 'Required';
    }

    if (control.errors?.['min']) {
      return 'Minimum value is 0';
    }

    if (control.errors?.['minlength']) {
      return 'Must be at least 4 characters long!';
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
      height: 'auto',
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
    });
  }

  openEditImageModal(
    url: string,
    index: number,
    dialogTemplate: TemplateRef<any>
  ) {
    this.dialog.open(dialogTemplate, {
      data: { url: url, index: index },
      panelClass: 'no-border-dialog',
      disableClose: false,
      height: '400px',
      width: '800px',
    });
  }

  saveEditedSpec(data: { key: string; value: string }) {
    this.specs.set(data.key, data.value);
    this.filteredSpecs.set(data.key, data.value);
    this.dialog.closeAll();
  }

  saveEditedImage(data: { url: string; index: number }) {
    if (data.url.trim()) {
      this.pictures[data.index] = data.url.trim();
      this.dialog.closeAll();
    }
  }

  removePicture(index: number) {
    this.pictures.splice(index, 1);
    this.dialog.closeAll();
  }

  deleteSpec(key: string) {
    this.specs.delete(key);
    this.filteredSpecs.delete(key);
    this.dialog.closeAll();
  }

  addSpec() {
    const key = this.adForm.get('specKey')?.value?.trim();
    const value = this.adForm.get('specValue')?.value?.trim();
    if (key && value) {
      this.specs.set(key, value);
      this.filteredSpecs.set(key, value);
      this.adForm.get('specKey')?.setValue('');
      this.adForm.get('specValue')?.setValue('');
    }
  }

  get filteredSpecsEntries() {
    return this.filteredSpecs ? Array.from(this.filteredSpecs.entries()) : [];
  }

  addPicture() {
    const picture = this.adForm.get('image')?.value?.trim();
    if (picture) {
      this.pictures.push(picture);
      this.adForm.get('image')?.setValue('');
    }
  }
}
