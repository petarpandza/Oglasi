import { TestBed } from '@angular/core/testing';

import { AdCreationService } from './ad-creation.service';

describe('AdCreationService', () => {
  let service: AdCreationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdCreationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
