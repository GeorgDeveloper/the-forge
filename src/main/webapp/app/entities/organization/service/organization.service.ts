import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IOrganization, NewOrganization } from '../organization.model';

export type EntityResponseType = HttpResponse<IOrganization>;
export type EntityArrayResponseType = HttpResponse<IOrganization[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organizations');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(organization: NewOrganization): Observable<EntityResponseType> {
    return this.http.post<IOrganization>(this.resourceUrl, organization, { observe: 'response' });
  }

  update(organization: IOrganization): Observable<EntityResponseType> {
    return this.http.put<IOrganization>(`${this.resourceUrl}/${organization.id}`, organization, { observe: 'response' });
  }

  partialUpdate(organization: Partial<IOrganization> & Pick<IOrganization, 'id'>): Observable<EntityResponseType> {
    return this.http.patch<IOrganization>(`${this.resourceUrl}/${organization.id}`, organization, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrganization>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(params?: any): Observable<EntityArrayResponseType> {
    return this.http.get<IOrganization[]>(this.resourceUrl, { params, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrganizationIdentifier(org: Pick<IOrganization, 'id'>): number {
    return org.id;
  }
}
