import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VideoProfilePageComponent } from './video-profile-page.component';

describe('VideoProfilePageComponent', () => {
  let component: VideoProfilePageComponent;
  let fixture: ComponentFixture<VideoProfilePageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VideoProfilePageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VideoProfilePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
