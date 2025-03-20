import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProfession, NewProfession } from '../profession.model';

export type PartialUpdateProfession = Partial<IProfession> & Pick<IProfession, 'id'>;

export type EntityResponseType = HttpResponse<IProfession>;
export type EntityArrayResponseType = HttpResponse<IProfession[]>;

@Injectable({ providedIn: 'root' })
export class ProfessionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/professions');

  create(profession: NewProfession): Observable<EntityResponseType> {
    return this.http.post<IProfession>(this.resourceUrl, profession, { observe: 'response' });
  }

  update(profession: IProfession): Observable<EntityResponseType> {
    return this.http.put<IProfession>(`${this.resourceUrl}/${this.getProfessionIdentifier(profession)}`, profession, {
      observe: 'response',
    });
  }

  partialUpdate(profession: PartialUpdateProfession): Observable<EntityResponseType> {
    return this.http.patch<IProfession>(`${this.resourceUrl}/${this.getProfessionIdentifier(profession)}`, profession, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProfession>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfession[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProfessionIdentifier(profession: Pick<IProfession, 'id'>): number {
    return profession.id;
  }

  compareProfession(o1: Pick<IProfession, 'id'> | null, o2: Pick<IProfession, 'id'> | null): boolean {
    return o1 && o2 ? this.getProfessionIdentifier(o1) === this.getProfessionIdentifier(o2) : o1 === o2;
  }

  addProfessionToCollectionIfMissing<Type extends Pick<IProfession, 'id'>>(
    professionCollection: Type[],
    ...professionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const professions: Type[] = professionsToCheck.filter(isPresent);
    if (professions.length > 0) {
      const professionCollectionIdentifiers = professionCollection.map(professionItem => this.getProfessionIdentifier(professionItem));
      const professionsToAdd = professions.filter(professionItem => {
        const professionIdentifier = this.getProfessionIdentifier(professionItem);
        if (professionCollectionIdentifiers.includes(professionIdentifier)) {
          return false;
        }
        professionCollectionIdentifiers.push(professionIdentifier);
        return true;
      });
      return [...professionsToAdd, ...professionCollection];
    }
    return professionCollection;
  }
}
