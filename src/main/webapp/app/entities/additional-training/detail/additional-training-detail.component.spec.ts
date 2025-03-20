import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AdditionalTrainingDetailComponent } from './additional-training-detail.component';

describe('AdditionalTraining Management Detail Component', () => {
  let comp: AdditionalTrainingDetailComponent;
  let fixture: ComponentFixture<AdditionalTrainingDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdditionalTrainingDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./additional-training-detail.component').then(m => m.AdditionalTrainingDetailComponent),
              resolve: { additionalTraining: () => of({ id: 2143 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AdditionalTrainingDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdditionalTrainingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load additionalTraining on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AdditionalTrainingDetailComponent);

      // THEN
      expect(instance.additionalTraining()).toEqual(expect.objectContaining({ id: 2143 }));
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
