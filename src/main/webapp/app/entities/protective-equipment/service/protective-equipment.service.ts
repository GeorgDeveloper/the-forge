import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProtectiveEquipment, NewProtectiveEquipment } from '../protective-equipment.model';

export type PartialUpdateProtectiveEquipment = Partial<IProtectiveEquipment> & Pick<IProtectiveEquipment, 'id'>;

export type EntityResponseType = HttpResponse<IProtectiveEquipment>;
export type EntityArrayResponseType = HttpResponse<IProtectiveEquipment[]>;

@Injectable({ providedIn: 'root' })
export class ProtectiveEquipmentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/protective-equipments');

  create(protectiveEquipment: NewProtectiveEquipment): Observable<EntityResponseType> {
    return this.http.post<IProtectiveEquipment>(this.resourceUrl, protectiveEquipment, { observe: 'response' });
  }

  update(protectiveEquipment: IProtectiveEquipment): Observable<EntityResponseType> {
    return this.http.put<IProtectiveEquipment>(
      `${this.resourceUrl}/${this.getProtectiveEquipmentIdentifier(protectiveEquipment)}`,
      protectiveEquipment,
      { observe: 'response' },
    );
  }

  partialUpdate(protectiveEquipment: PartialUpdateProtectiveEquipment): Observable<EntityResponseType> {
    return this.http.patch<IProtectiveEquipment>(
      `${this.resourceUrl}/${this.getProtectiveEquipmentIdentifier(protectiveEquipment)}`,
      protectiveEquipment,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProtectiveEquipment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProtectiveEquipment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProtectiveEquipmentIdentifier(protectiveEquipment: Pick<IProtectiveEquipment, 'id'>): number {
    return protectiveEquipment.id;
  }

  compareProtectiveEquipment(o1: Pick<IProtectiveEquipment, 'id'> | null, o2: Pick<IProtectiveEquipment, 'id'> | null): boolean {
    return o1 && o2 ? this.getProtectiveEquipmentIdentifier(o1) === this.getProtectiveEquipmentIdentifier(o2) : o1 === o2;
  }

  addProtectiveEquipmentToCollectionIfMissing<Type extends Pick<IProtectiveEquipment, 'id'>>(
    protectiveEquipmentCollection: Type[],
    ...protectiveEquipmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const protectiveEquipments: Type[] = protectiveEquipmentsToCheck.filter(isPresent);
    if (protectiveEquipments.length > 0) {
      const protectiveEquipmentCollectionIdentifiers = protectiveEquipmentCollection.map(protectiveEquipmentItem =>
        this.getProtectiveEquipmentIdentifier(protectiveEquipmentItem),
      );
      const protectiveEquipmentsToAdd = protectiveEquipments.filter(protectiveEquipmentItem => {
        const protectiveEquipmentIdentifier = this.getProtectiveEquipmentIdentifier(protectiveEquipmentItem);
        if (protectiveEquipmentCollectionIdentifiers.includes(protectiveEquipmentIdentifier)) {
          return false;
        }
        protectiveEquipmentCollectionIdentifiers.push(protectiveEquipmentIdentifier);
        return true;
      });
      return [...protectiveEquipmentsToAdd, ...protectiveEquipmentCollection];
    }
    return protectiveEquipmentCollection;
  }
}
