import { Injectable } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { IOrganization, NewOrganization } from '../organization.model';

type OrganizationFormGroupInput = IOrganization | Partial<NewOrganization>;

@Injectable({ providedIn: 'root' })
export class OrganizationFormService {
  constructor(private fb: FormBuilder) {}

  createForm(organization: OrganizationFormGroupInput = { id: null }): any {
    const orgRaw = { id: null, ...organization } as IOrganization | NewOrganization;
    return this.fb.group({
      id: [{ value: orgRaw.id, disabled: true }],
      fullName: [orgRaw.fullName, [Validators.required]],
      shortName: [orgRaw.shortName, [Validators.required]],
      tagline: [orgRaw.tagline, [Validators.required]],
      activityAreas: [orgRaw.activityAreas],
      legalAddress: [orgRaw.legalAddress],
      actualAddress: [orgRaw.actualAddress],
      inn: [orgRaw.inn],
      kpp: [orgRaw.kpp],
      ogrn: [orgRaw.ogrn],
      okpo: [orgRaw.okpo],
      bankName: [orgRaw.bankName],
      bankBik: [orgRaw.bankBik],
      bankCorrAccount: [orgRaw.bankCorrAccount],
      bankSettlementAccount: [orgRaw.bankSettlementAccount],
      phoneMain: [orgRaw.phoneMain],
      phoneSales: [orgRaw.phoneSales],
      phoneSupport: [orgRaw.phoneSupport],
      emailMain: [orgRaw.emailMain],
      emailPartners: [orgRaw.emailPartners],
      emailSupport: [orgRaw.emailSupport],
      website: [orgRaw.website],
      foundedYear: [orgRaw.foundedYear],
      employeesCountRange: [orgRaw.employeesCountRange],
      keyPersons: [orgRaw.keyPersons],
      products: [orgRaw.products],
      partners: [orgRaw.partners],
    });
  }

  getOrganization(form: any): IOrganization | NewOrganization {
    return form.getRawValue();
  }
}
