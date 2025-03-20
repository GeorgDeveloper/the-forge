import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IJobDescription, NewJobDescription } from '../job-description.model';

export type PartialUpdateJobDescription = Partial<IJobDescription> & Pick<IJobDescription, 'id'>;

type RestOf<T extends IJobDescription | NewJobDescription> = Omit<T, 'approvalDate'> & {
  approvalDate?: string | null;
};

export type RestJobDescription = RestOf<IJobDescription>;

export type NewRestJobDescription = RestOf<NewJobDescription>;

export type PartialUpdateRestJobDescription = RestOf<PartialUpdateJobDescription>;

export type EntityResponseType = HttpResponse<IJobDescription>;
export type EntityArrayResponseType = HttpResponse<IJobDescription[]>;

@Injectable({ providedIn: 'root' })
export class JobDescriptionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/job-descriptions');

  create(jobDescription: NewJobDescription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jobDescription);
    return this.http
      .post<RestJobDescription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(jobDescription: IJobDescription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jobDescription);
    return this.http
      .put<RestJobDescription>(`${this.resourceUrl}/${this.getJobDescriptionIdentifier(jobDescription)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(jobDescription: PartialUpdateJobDescription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jobDescription);
    return this.http
      .patch<RestJobDescription>(`${this.resourceUrl}/${this.getJobDescriptionIdentifier(jobDescription)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestJobDescription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestJobDescription[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getJobDescriptionIdentifier(jobDescription: Pick<IJobDescription, 'id'>): number {
    return jobDescription.id;
  }

  compareJobDescription(o1: Pick<IJobDescription, 'id'> | null, o2: Pick<IJobDescription, 'id'> | null): boolean {
    return o1 && o2 ? this.getJobDescriptionIdentifier(o1) === this.getJobDescriptionIdentifier(o2) : o1 === o2;
  }

  addJobDescriptionToCollectionIfMissing<Type extends Pick<IJobDescription, 'id'>>(
    jobDescriptionCollection: Type[],
    ...jobDescriptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const jobDescriptions: Type[] = jobDescriptionsToCheck.filter(isPresent);
    if (jobDescriptions.length > 0) {
      const jobDescriptionCollectionIdentifiers = jobDescriptionCollection.map(jobDescriptionItem =>
        this.getJobDescriptionIdentifier(jobDescriptionItem),
      );
      const jobDescriptionsToAdd = jobDescriptions.filter(jobDescriptionItem => {
        const jobDescriptionIdentifier = this.getJobDescriptionIdentifier(jobDescriptionItem);
        if (jobDescriptionCollectionIdentifiers.includes(jobDescriptionIdentifier)) {
          return false;
        }
        jobDescriptionCollectionIdentifiers.push(jobDescriptionIdentifier);
        return true;
      });
      return [...jobDescriptionsToAdd, ...jobDescriptionCollection];
    }
    return jobDescriptionCollection;
  }

  protected convertDateFromClient<T extends IJobDescription | NewJobDescription | PartialUpdateJobDescription>(
    jobDescription: T,
  ): RestOf<T> {
    return {
      ...jobDescription,
      approvalDate: jobDescription.approvalDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restJobDescription: RestJobDescription): IJobDescription {
    return {
      ...restJobDescription,
      approvalDate: restJobDescription.approvalDate ? dayjs(restJobDescription.approvalDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestJobDescription>): HttpResponse<IJobDescription> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestJobDescription[]>): HttpResponse<IJobDescription[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
