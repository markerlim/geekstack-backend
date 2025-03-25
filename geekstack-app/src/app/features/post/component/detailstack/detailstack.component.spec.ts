import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailstackComponent } from './detailstack.component';

describe('DetailstackComponent', () => {
  let component: DetailstackComponent;
  let fixture: ComponentFixture<DetailstackComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailstackComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailstackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
