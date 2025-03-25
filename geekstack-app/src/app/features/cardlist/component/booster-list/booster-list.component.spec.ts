import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoosterListComponent } from './booster-list.component';

describe('BoosterListComponent', () => {
  let component: BoosterListComponent;
  let fixture: ComponentFixture<BoosterListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BoosterListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BoosterListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
