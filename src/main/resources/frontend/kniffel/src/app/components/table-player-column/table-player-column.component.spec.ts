import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TablePlayerColumnComponent } from './table-player-column.component';

describe('TablePlayerColumnComponent', () => {
  let component: TablePlayerColumnComponent;
  let fixture: ComponentFixture<TablePlayerColumnComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TablePlayerColumnComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TablePlayerColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
