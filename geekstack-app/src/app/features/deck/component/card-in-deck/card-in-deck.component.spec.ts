import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardInDeckComponent } from './card-in-deck.component';

describe('CardInDeckComponent', () => {
  let component: CardInDeckComponent;
  let fixture: ComponentFixture<CardInDeckComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardInDeckComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CardInDeckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
