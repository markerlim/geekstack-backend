import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TcgImageComponent } from './tcg-image.component';

describe('TcgImageComponent', () => {
  let component: TcgImageComponent;
  let fixture: ComponentFixture<TcgImageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TcgImageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TcgImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
