import { TestBed } from '@angular/core/testing';

import { PathUtilsService } from './path-utils.service';

describe('PathUtilsService', () => {
  let service: PathUtilsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PathUtilsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
