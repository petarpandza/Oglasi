import { Component, OnInit, TemplateRef } from '@angular/core';
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
import { adInfo } from '../models/ad-classes';
import { Subject } from 'rxjs';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { AdService } from '../../services/ad.service';
import { CityService } from '../../services/city.service';
import { City } from '../models/city';
import { OglasiConstants } from '../../oglasi-constants';

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
    FormsModule,
  ],
  templateUrl: './create-ad.component.html',
  styleUrl: './create-ad.component.css',
})
export class CreateAdComponent implements OnInit {
  showSuccessMessage: boolean = false;
  adForm!: FormGroup;
  cities: City[] = [];
  filteredCities: City[] = [];
  private _onDestroy = new Subject<void>();
  specs = new Map<string, string>();
  filteredSpecs = new Map<string, string>();
  pictures: string[] = [];
  editSpecData = { key: '', value: '' };

  constructor(
    private dialog: MatDialog,
    private adService: AdService,
    private cityService: CityService
  ) {}

  ngOnInit() {
    this.cityService.getCities().subscribe({
      next: (cities: City[]) => {
        this.cities = cities;
        this.filteredCities = cities;
      },
      error: (err) => {
        console.error('Error fetching cities:', err);
      },
    });

    this.adForm = new FormGroup({
      title: new FormControl('', [
        Validators.required,
        Validators.minLength(4),
      ]),
      price: new FormControl('', [Validators.required, Validators.min(0)]),
      shortDesc: new FormControl(''),
      longDesc: new FormControl(''),
      city: new FormControl(0, [Validators.required]),
      type: new FormControl(0, [Validators.min(1)]),
      state: new FormControl(0, [Validators.min(1)]),
      specKey: new FormControl(''),
      specValue: new FormControl(''),
      image: new FormControl(''),
    });
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
  }

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

  addPicture() {
    const picture = this.adForm.get('image')?.value?.trim();
    if (picture) {
      this.pictures.push(picture);
      this.adForm.get('image')?.setValue('');
    }
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
    return Array.from(this.filteredSpecs.entries());
  }

  deleteSpec(key: string): void {
    this.specs.delete(key);
    this.filteredSpecs.delete(key);
    this.dialog.closeAll();
  }

  ngOnDestroy() {
    this._onDestroy.next();
    this._onDestroy.complete();
  }

  onSubmit() {
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
      next: (response) => {
        if (response) {
          this.showSuccessMessage = true;
          setTimeout(() => {
            this.showSuccessMessage = false;
            this.adForm.reset();
            this.specs.clear();
            this.pictures.length = 0;
          }, 3000);
        }
      },
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

  private dynamicStateValidator(form: FormGroup): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const type = form.get('type')?.value;
      return control.value === OglasiConstants.STATE_BOTH &&
        type !== OglasiConstants.TYPE_BUY
        ? { invalidState: true }
        : null;
    };
  }
}
