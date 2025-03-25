import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardListDisplayComponent } from './card-list-display.component';

describe('CardListDisplayComponent', () => {
  let component: CardListDisplayComponent;
  let fixture: ComponentFixture<CardListDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardListDisplayComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CardListDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
