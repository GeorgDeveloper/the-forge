import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IOrganization } from '../organization.model';
import { EmployeesCountRange } from '../../enumerations/employees-count-range.model';

@Component({
  standalone: true,
  selector: 'jhi-organization-detail',
  templateUrl: './organization-detail.component.html',
  imports: [RouterModule, SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrganizationDetailComponent {
  protected readonly activatedRoute = inject(ActivatedRoute);
  organization: IOrganization | null = null;
  employeesCountRange = EmployeesCountRange;

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organization }) => (this.organization = organization));
  }
}
