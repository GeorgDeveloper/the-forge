import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SafetyInstructionDetailComponent } from './safety-instruction-detail.component';

describe('SafetyInstruction Management Detail Component', () => {
  let comp: SafetyInstructionDetailComponent;
  let fixture: ComponentFixture<SafetyInstructionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SafetyInstructionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./safety-instruction-detail.component').then(m => m.SafetyInstructionDetailComponent),
              resolve: { safetyInstruction: () => of({ id: 5402 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SafetyInstructionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SafetyInstructionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load safetyInstruction on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SafetyInstructionDetailComponent);

      // THEN
      expect(instance.safetyInstruction()).toEqual(expect.objectContaining({ id: 5402 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
