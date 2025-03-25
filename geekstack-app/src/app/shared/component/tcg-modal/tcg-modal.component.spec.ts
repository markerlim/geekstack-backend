import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TcgModalComponent } from './tcg-modal.component';

describe('TcgModalComponent', () => {
  let component: TcgModalComponent;
  let fixture: ComponentFixture<TcgModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TcgModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TcgModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
