import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeckloadSelectComponent } from './deckload-select.component';

describe('DeckloadSelectComponent', () => {
  let component: DeckloadSelectComponent;
  let fixture: ComponentFixture<DeckloadSelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeckloadSelectComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeckloadSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
