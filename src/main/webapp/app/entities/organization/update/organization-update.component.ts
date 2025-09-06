import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IOrganization, NewOrganization } from '../organization.model';
import { OrganizationService } from '../service/organization.service';
import { OrganizationFormService } from './organization-form.service';

@Component({
  standalone: true,
  selector: 'jhi-organization-update',
  templateUrl: './organization-update.component.html',
  imports: [RouterModule, SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrganizationUpdateComponent implements OnInit {
  isSaving = false;
  organization: IOrganization | null = null;

  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly router = inject(Router);
  protected readonly organizationService = inject(OrganizationService);
  protected readonly organizationFormService = inject(OrganizationFormService);

  editForm = this.organizationFormService.createForm();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organization }) => {
      this.organization = organization;
      if (organization) {
        this.editForm.reset({ ...organization });
      }
    });
  }

  save(): void {
    this.isSaving = true;
    const org = this.organizationFormService.getOrganization(this.editForm);
    if (org.id != null) {
      this.subscribeToSaveResponse(this.organizationService.update(org as IOrganization));
    } else {
      this.subscribeToSaveResponse(this.organizationService.create(org as NewOrganization));
    }
  }

  protected subscribeToSaveResponse(result: any): void {
    result.subscribe({
      next: (res: HttpResponse<IOrganization>) => this.onSaveSuccess(res.body!),
      error: () => (this.isSaving = false),
    });
  }

  protected onSaveSuccess(_entity: IOrganization): void {
    this.isSaving = false;
    this.router.navigate(['/organization']);
  }
}
