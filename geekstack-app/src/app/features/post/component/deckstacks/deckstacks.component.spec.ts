import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeckstacksComponent } from './deckstacks.component';

describe('DeckstacksComponent', () => {
  let component: DeckstacksComponent;
  let fixture: ComponentFixture<DeckstacksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeckstacksComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeckstacksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
