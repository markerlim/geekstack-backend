import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeckbuilderActionsComponent } from './deckbuilder-actions.component';

describe('DeckbuilderActionsComponent', () => {
  let component: DeckbuilderActionsComponent;
  let fixture: ComponentFixture<DeckbuilderActionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeckbuilderActionsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeckbuilderActionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
