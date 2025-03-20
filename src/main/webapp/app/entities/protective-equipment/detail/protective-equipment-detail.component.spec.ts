import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProtectiveEquipmentDetailComponent } from './protective-equipment-detail.component';

describe('ProtectiveEquipment Management Detail Component', () => {
  let comp: ProtectiveEquipmentDetailComponent;
  let fixture: ComponentFixture<ProtectiveEquipmentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProtectiveEquipmentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./protective-equipment-detail.component').then(m => m.ProtectiveEquipmentDetailComponent),
              resolve: { protectiveEquipment: () => of({ id: 26931 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProtectiveEquipmentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProtectiveEquipmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load protectiveEquipment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProtectiveEquipmentDetailComponent);

      // THEN
      expect(instance.protectiveEquipment()).toEqual(expect.objectContaining({ id: 26931 }));
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
