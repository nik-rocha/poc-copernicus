import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MainCollaboratorsComponent } from './main-collaborators.component';

describe('MainCollaboratorsComponent', () => {
  let component: MainCollaboratorsComponent;
  let fixture: ComponentFixture<MainCollaboratorsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MainCollaboratorsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(MainCollaboratorsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
