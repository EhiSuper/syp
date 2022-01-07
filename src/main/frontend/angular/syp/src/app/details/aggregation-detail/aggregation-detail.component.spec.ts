import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AggregationDetailComponent } from './aggregation-detail.component';

describe('AggregationDetailComponent', () => {
  let component: AggregationDetailComponent;
  let fixture: ComponentFixture<AggregationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AggregationDetailComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AggregationDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
