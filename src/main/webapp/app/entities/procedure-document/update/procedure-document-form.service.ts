import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProcedureDocument, NewProcedureDocument } from '../procedure-document.model';

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProcedureDocument for edit and NewProcedureDocumentFormGroupInput for create.
 */
type ProcedureDocumentFormGroupInput = IProcedureDocument | PartialWithRequiredKeyOf<NewProcedureDocument>;

type PartialWithRequiredKeyOf<T> = Partial<Omit<T, 'id'>> & { id: T extends { id: infer U } ? U : never };

export type ProcedureDocumentFormGroup = FormGroup<ProcedureDocumentFormGroupContent>;

export interface ProcedureDocumentFormGroupContent {
  id: FormControl<IProcedureDocument['id'] | NewProcedureDocument['id']>;
  documentName: FormControl<IProcedureDocument['documentName']>;
  introductionDate: FormControl<IProcedureDocument['introductionDate']>;
  description: FormControl<IProcedureDocument['description']>;
  profession: FormControl<IProcedureDocument['profession']>;
  position: FormControl<IProcedureDocument['position']>;
}

@Injectable({ providedIn: 'root' })
export class ProcedureDocumentFormService {
  createProcedureDocumentFormGroup(procedureDocument: ProcedureDocumentFormGroupInput = { id: null }): ProcedureDocumentFormGroup {
    const procedureDocumentRawValue = {
      ...this.getFormDefaults(),
      ...procedureDocument,
    };
    return new FormGroup<ProcedureDocumentFormGroupContent>({
      id: new FormControl(
        { value: procedureDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentName: new FormControl(procedureDocumentRawValue.documentName, {
        validators: [Validators.required],
      }),
      introductionDate: new FormControl(procedureDocumentRawValue.introductionDate, {
        validators: [Validators.required],
      }),
      description: new FormControl(procedureDocumentRawValue.description),
      profession: new FormControl(procedureDocumentRawValue.profession),
      position: new FormControl(procedureDocumentRawValue.position),
    });
  }

  getProcedureDocument(form: ProcedureDocumentFormGroup): IProcedureDocument | NewProcedureDocument {
    return form.getRawValue() as IProcedureDocument | NewProcedureDocument;
  }

  resetForm(form: ProcedureDocumentFormGroup, procedureDocument: ProcedureDocumentFormGroupInput): void {
    const procedureDocumentRawValue = { ...this.getFormDefaults(), ...procedureDocument };
    form.reset(
      {
        ...procedureDocumentRawValue,
        id: { value: procedureDocumentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProcedureDocumentFormDefaults {
    return {
      id: null,
    };
  }
}

type ProcedureDocumentFormDefaults = Pick<NewProcedureDocument, 'id'>;
