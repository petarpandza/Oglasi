import { TestBed } from '@angular/core/testing';

import { AdService } from './ad.service';

describe('AdCreationService', () => {
  let service: AdService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
