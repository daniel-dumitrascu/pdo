import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterSectionComponent } from './register-section.component';

describe('RegisterSectionComponent', () => {
  let component: RegisterSectionComponent;
  let fixture: ComponentFixture<RegisterSectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegisterSectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
