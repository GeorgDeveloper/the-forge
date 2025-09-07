import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProcedureDocument, NewProcedureDocument } from '../procedure-document.model';

export type PartialUpdateProcedureDocument = Partial<IProcedureDocument> & Pick<IProcedureDocument, 'id'>;

type RestOf<T extends IProcedureDocument | NewProcedureDocument> = Omit<T, 'introductionDate'> & {
  introductionDate?: string | null;
};

export type RestProcedureDocument = RestOf<IProcedureDocument>;

export type NewRestProcedureDocument = RestOf<NewProcedureDocument>;

export type PartialUpdateRestProcedureDocument = RestOf<PartialUpdateProcedureDocument>;

export type EntityResponseType = HttpResponse<IProcedureDocument>;
export type EntityArrayResponseType = HttpResponse<IProcedureDocument[]>;

@Injectable({ providedIn: 'root' })
export class ProcedureDocumentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/procedure-documents');

  create(procedureDocument: NewProcedureDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(procedureDocument);
    return this.http
      .post<RestProcedureDocument>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(procedureDocument: IProcedureDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(procedureDocument);
    return this.http
      .put<RestProcedureDocument>(`${this.resourceUrl}/${this.getProcedureDocumentIdentifier(procedureDocument)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(procedureDocument: PartialUpdateProcedureDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(procedureDocument);
    return this.http
      .patch<RestProcedureDocument>(`${this.resourceUrl}/${this.getProcedureDocumentIdentifier(procedureDocument)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProcedureDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProcedureDocument[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  uploadPdfFile(id: number, file: File): Observable<EntityResponseType> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http
      .post<RestProcedureDocument>(`${this.resourceUrl}/${id}/pdf`, formData, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  downloadPdfFile(id: number): Observable<Blob> {
    return this.http.get(`${this.resourceUrl}/${id}/pdf`, { responseType: 'blob' });
  }

  deletePdfFile(id: number): Observable<EntityResponseType> {
    return this.http
      .delete<RestProcedureDocument>(`${this.resourceUrl}/${id}/pdf`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  getProcedureDocumentIdentifier(procedureDocument: Pick<IProcedureDocument, 'id'>): number {
    return procedureDocument.id;
  }

  compareProcedureDocument(o1: Pick<IProcedureDocument, 'id'> | null, o2: Pick<IProcedureDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getProcedureDocumentIdentifier(o1) === this.getProcedureDocumentIdentifier(o2) : o1 === o2;
  }

  addProcedureDocumentToCollectionIfMissing<Type extends Pick<IProcedureDocument, 'id'>>(
    procedureDocumentCollection: Type[],
    ...procedureDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const procedureDocuments: Type[] = procedureDocumentsToCheck.filter(isPresent);
    if (procedureDocuments.length > 0) {
      const procedureDocumentCollectionIdentifiers = procedureDocumentCollection.map(procedureDocumentItem =>
        this.getProcedureDocumentIdentifier(procedureDocumentItem),
      );
      const procedureDocumentsToAdd = procedureDocuments.filter(procedureDocumentItem => {
        const procedureDocumentIdentifier = this.getProcedureDocumentIdentifier(procedureDocumentItem);
        if (procedureDocumentCollectionIdentifiers.includes(procedureDocumentIdentifier)) {
          return false;
        }
        procedureDocumentCollectionIdentifiers.push(procedureDocumentIdentifier);
        return true;
      });
      return [...procedureDocumentsToAdd, ...procedureDocumentCollection];
    }
    return procedureDocumentCollection;
  }

  protected convertDateFromClient<T extends IProcedureDocument | NewProcedureDocument | PartialUpdateProcedureDocument>(
    procedureDocument: T,
  ): RestOf<T> {
    return {
      ...procedureDocument,
      introductionDate: procedureDocument.introductionDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restProcedureDocument: RestProcedureDocument): IProcedureDocument {
    return {
      ...restProcedureDocument,
      introductionDate: restProcedureDocument.introductionDate ? dayjs(restProcedureDocument.introductionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProcedureDocument>): HttpResponse<IProcedureDocument> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProcedureDocument[]>): HttpResponse<IProcedureDocument[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
