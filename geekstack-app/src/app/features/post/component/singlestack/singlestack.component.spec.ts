import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SinglestackComponent } from './singlestack.component';

describe('SinglestackComponent', () => {
  let component: SinglestackComponent;
  let fixture: ComponentFixture<SinglestackComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SinglestackComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SinglestackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
