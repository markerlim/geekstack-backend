import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeftstackComponent } from './leftstack.component';

describe('LeftstackComponent', () => {
  let component: LeftstackComponent;
  let fixture: ComponentFixture<LeftstackComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LeftstackComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LeftstackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
