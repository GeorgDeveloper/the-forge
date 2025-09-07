import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISafetyInstruction, NewSafetyInstruction } from '../safety-instruction.model';

export type PartialUpdateSafetyInstruction = Partial<ISafetyInstruction> & Pick<ISafetyInstruction, 'id'>;

type RestOf<T extends ISafetyInstruction | NewSafetyInstruction> = Omit<T, 'introductionDate'> & {
  introductionDate?: string | null;
};

export type RestSafetyInstruction = RestOf<ISafetyInstruction>;

export type NewRestSafetyInstruction = RestOf<NewSafetyInstruction>;

export type PartialUpdateRestSafetyInstruction = RestOf<PartialUpdateSafetyInstruction>;

export type EntityResponseType = HttpResponse<ISafetyInstruction>;
export type EntityArrayResponseType = HttpResponse<ISafetyInstruction[]>;

@Injectable({ providedIn: 'root' })
export class SafetyInstructionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/safety-instructions');

  create(safetyInstruction: NewSafetyInstruction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(safetyInstruction);
    return this.http
      .post<RestSafetyInstruction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(safetyInstruction: ISafetyInstruction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(safetyInstruction);
    return this.http
      .put<RestSafetyInstruction>(`${this.resourceUrl}/${this.getSafetyInstructionIdentifier(safetyInstruction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(safetyInstruction: PartialUpdateSafetyInstruction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(safetyInstruction);
    return this.http
      .patch<RestSafetyInstruction>(`${this.resourceUrl}/${this.getSafetyInstructionIdentifier(safetyInstruction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSafetyInstruction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSafetyInstruction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  uploadPdfFile(id: number, file: File): Observable<EntityResponseType> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http
      .post<RestSafetyInstruction>(`${this.resourceUrl}/${id}/pdf`, formData, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  downloadPdfFile(id: number): Observable<Blob> {
    return this.http.get(`${this.resourceUrl}/${id}/pdf`, { responseType: 'blob' });
  }

  deletePdfFile(id: number): Observable<EntityResponseType> {
    return this.http
      .delete<RestSafetyInstruction>(`${this.resourceUrl}/${id}/pdf`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  getSafetyInstructionIdentifier(safetyInstruction: Pick<ISafetyInstruction, 'id'>): number {
    return safetyInstruction.id;
  }

  compareSafetyInstruction(o1: Pick<ISafetyInstruction, 'id'> | null, o2: Pick<ISafetyInstruction, 'id'> | null): boolean {
    return o1 && o2 ? this.getSafetyInstructionIdentifier(o1) === this.getSafetyInstructionIdentifier(o2) : o1 === o2;
  }

  addSafetyInstructionToCollectionIfMissing<Type extends Pick<ISafetyInstruction, 'id'>>(
    safetyInstructionCollection: Type[],
    ...safetyInstructionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const safetyInstructions: Type[] = safetyInstructionsToCheck.filter(isPresent);
    if (safetyInstructions.length > 0) {
      const safetyInstructionCollectionIdentifiers = safetyInstructionCollection.map(safetyInstructionItem =>
        this.getSafetyInstructionIdentifier(safetyInstructionItem),
      );
      const safetyInstructionsToAdd = safetyInstructions.filter(safetyInstructionItem => {
        const safetyInstructionIdentifier = this.getSafetyInstructionIdentifier(safetyInstructionItem);
        if (safetyInstructionCollectionIdentifiers.includes(safetyInstructionIdentifier)) {
          return false;
        }
        safetyInstructionCollectionIdentifiers.push(safetyInstructionIdentifier);
        return true;
      });
      return [...safetyInstructionsToAdd, ...safetyInstructionCollection];
    }
    return safetyInstructionCollection;
  }

  protected convertDateFromClient<T extends ISafetyInstruction | NewSafetyInstruction | PartialUpdateSafetyInstruction>(
    safetyInstruction: T,
  ): RestOf<T> {
    return {
      ...safetyInstruction,
      introductionDate: safetyInstruction.introductionDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSafetyInstruction: RestSafetyInstruction): ISafetyInstruction {
    return {
      ...restSafetyInstruction,
      introductionDate: restSafetyInstruction.introductionDate ? dayjs(restSafetyInstruction.introductionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSafetyInstruction>): HttpResponse<ISafetyInstruction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSafetyInstruction[]>): HttpResponse<ISafetyInstruction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
