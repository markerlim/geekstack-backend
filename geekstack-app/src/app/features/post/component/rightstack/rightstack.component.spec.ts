import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RightstackComponent } from './rightstack.component';

describe('RightstackComponent', () => {
  let component: RightstackComponent;
  let fixture: ComponentFixture<RightstackComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RightstackComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RightstackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
