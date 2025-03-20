import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPosition, NewPosition } from '../position.model';

export type PartialUpdatePosition = Partial<IPosition> & Pick<IPosition, 'id'>;

export type EntityResponseType = HttpResponse<IPosition>;
export type EntityArrayResponseType = HttpResponse<IPosition[]>;

@Injectable({ providedIn: 'root' })
export class PositionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/positions');

  create(position: NewPosition): Observable<EntityResponseType> {
    return this.http.post<IPosition>(this.resourceUrl, position, { observe: 'response' });
  }

  update(position: IPosition): Observable<EntityResponseType> {
    return this.http.put<IPosition>(`${this.resourceUrl}/${this.getPositionIdentifier(position)}`, position, { observe: 'response' });
  }

  partialUpdate(position: PartialUpdatePosition): Observable<EntityResponseType> {
    return this.http.patch<IPosition>(`${this.resourceUrl}/${this.getPositionIdentifier(position)}`, position, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPosition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPosition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPositionIdentifier(position: Pick<IPosition, 'id'>): number {
    return position.id;
  }

  comparePosition(o1: Pick<IPosition, 'id'> | null, o2: Pick<IPosition, 'id'> | null): boolean {
    return o1 && o2 ? this.getPositionIdentifier(o1) === this.getPositionIdentifier(o2) : o1 === o2;
  }

  addPositionToCollectionIfMissing<Type extends Pick<IPosition, 'id'>>(
    positionCollection: Type[],
    ...positionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const positions: Type[] = positionsToCheck.filter(isPresent);
    if (positions.length > 0) {
      const positionCollectionIdentifiers = positionCollection.map(positionItem => this.getPositionIdentifier(positionItem));
      const positionsToAdd = positions.filter(positionItem => {
        const positionIdentifier = this.getPositionIdentifier(positionItem);
        if (positionCollectionIdentifiers.includes(positionIdentifier)) {
          return false;
        }
        positionCollectionIdentifiers.push(positionIdentifier);
        return true;
      });
      return [...positionsToAdd, ...positionCollection];
    }
    return positionCollection;
  }
}
