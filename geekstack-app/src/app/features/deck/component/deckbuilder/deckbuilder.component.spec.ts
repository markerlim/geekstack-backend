import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeckbuilderComponent } from './deckbuilder.component';

describe('DeckbuilderComponent', () => {
  let component: DeckbuilderComponent;
  let fixture: ComponentFixture<DeckbuilderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeckbuilderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeckbuilderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
