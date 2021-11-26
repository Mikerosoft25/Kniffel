import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableDefinitionColumnComponent } from './table-definition-column.component';

describe('TableDefinitionColumnComponent', () => {
  let component: TableDefinitionColumnComponent;
  let fixture: ComponentFixture<TableDefinitionColumnComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TableDefinitionColumnComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TableDefinitionColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
