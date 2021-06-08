import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterResultSectionComponent } from './filter-result-section.component';

describe('FilterResultSectionComponent', () => {
  let component: FilterResultSectionComponent;
  let fixture: ComponentFixture<FilterResultSectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FilterResultSectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilterResultSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
