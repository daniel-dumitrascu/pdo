import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterControlSectionComponent } from './filter-control-section.component';

describe('FilterControlSectionComponent', () => {
  let component: FilterControlSectionComponent;
  let fixture: ComponentFixture<FilterControlSectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FilterControlSectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilterControlSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
