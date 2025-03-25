import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostcontentComponent } from './postcontent.component';

describe('PostcontentComponent', () => {
  let component: PostcontentComponent;
  let fixture: ComponentFixture<PostcontentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostcontentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PostcontentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
