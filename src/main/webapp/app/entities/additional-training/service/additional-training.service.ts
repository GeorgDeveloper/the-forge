import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdditionalTraining, NewAdditionalTraining } from '../additional-training.model';

export type PartialUpdateAdditionalTraining = Partial<IAdditionalTraining> & Pick<IAdditionalTraining, 'id'>;

type RestOf<T extends IAdditionalTraining | NewAdditionalTraining> = Omit<T, 'trainingDate' | 'nextTrainingDate'> & {
  trainingDate?: string | null;
  nextTrainingDate?: string | null;
};

export type RestAdditionalTraining = RestOf<IAdditionalTraining>;

export type NewRestAdditionalTraining = RestOf<NewAdditionalTraining>;

export type PartialUpdateRestAdditionalTraining = RestOf<PartialUpdateAdditionalTraining>;

export type EntityResponseType = HttpResponse<IAdditionalTraining>;
export type EntityArrayResponseType = HttpResponse<IAdditionalTraining[]>;

@Injectable({ providedIn: 'root' })
export class AdditionalTrainingService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/additional-trainings');

  create(additionalTraining: NewAdditionalTraining): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(additionalTraining);
    return this.http
      .post<RestAdditionalTraining>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(additionalTraining: IAdditionalTraining): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(additionalTraining);
    return this.http
      .put<RestAdditionalTraining>(`${this.resourceUrl}/${this.getAdditionalTrainingIdentifier(additionalTraining)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(additionalTraining: PartialUpdateAdditionalTraining): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(additionalTraining);
    return this.http
      .patch<RestAdditionalTraining>(`${this.resourceUrl}/${this.getAdditionalTrainingIdentifier(additionalTraining)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAdditionalTraining>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAdditionalTraining[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAdditionalTrainingIdentifier(additionalTraining: Pick<IAdditionalTraining, 'id'>): number {
    return additionalTraining.id;
  }

  compareAdditionalTraining(o1: Pick<IAdditionalTraining, 'id'> | null, o2: Pick<IAdditionalTraining, 'id'> | null): boolean {
    return o1 && o2 ? this.getAdditionalTrainingIdentifier(o1) === this.getAdditionalTrainingIdentifier(o2) : o1 === o2;
  }

  addAdditionalTrainingToCollectionIfMissing<Type extends Pick<IAdditionalTraining, 'id'>>(
    additionalTrainingCollection: Type[],
    ...additionalTrainingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const additionalTrainings: Type[] = additionalTrainingsToCheck.filter(isPresent);
    if (additionalTrainings.length > 0) {
      const additionalTrainingCollectionIdentifiers = additionalTrainingCollection.map(additionalTrainingItem =>
        this.getAdditionalTrainingIdentifier(additionalTrainingItem),
      );
      const additionalTrainingsToAdd = additionalTrainings.filter(additionalTrainingItem => {
        const additionalTrainingIdentifier = this.getAdditionalTrainingIdentifier(additionalTrainingItem);
        if (additionalTrainingCollectionIdentifiers.includes(additionalTrainingIdentifier)) {
          return false;
        }
        additionalTrainingCollectionIdentifiers.push(additionalTrainingIdentifier);
        return true;
      });
      return [...additionalTrainingsToAdd, ...additionalTrainingCollection];
    }
    return additionalTrainingCollection;
  }

  protected convertDateFromClient<T extends IAdditionalTraining | NewAdditionalTraining | PartialUpdateAdditionalTraining>(
    additionalTraining: T,
  ): RestOf<T> {
    return {
      ...additionalTraining,
      trainingDate: additionalTraining.trainingDate?.format(DATE_FORMAT) ?? null,
      nextTrainingDate: additionalTraining.nextTrainingDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAdditionalTraining: RestAdditionalTraining): IAdditionalTraining {
    return {
      ...restAdditionalTraining,
      trainingDate: restAdditionalTraining.trainingDate ? dayjs(restAdditionalTraining.trainingDate) : undefined,
      nextTrainingDate: restAdditionalTraining.nextTrainingDate ? dayjs(restAdditionalTraining.nextTrainingDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAdditionalTraining>): HttpResponse<IAdditionalTraining> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAdditionalTraining[]>): HttpResponse<IAdditionalTraining[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
