import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeckcoverSelectComponent } from './deckcover-select.component';

describe('DeckcoverSelectComponent', () => {
  let component: DeckcoverSelectComponent;
  let fixture: ComponentFixture<DeckcoverSelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeckcoverSelectComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeckcoverSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
