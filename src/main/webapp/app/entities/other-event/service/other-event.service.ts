import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IOtherEvent, NewOtherEvent } from '../other-event.model';

export type EntityResponseType = HttpResponse<IOtherEvent>;
export type EntityArrayResponseType = HttpResponse<IOtherEvent[]>;

type RestOf<T extends IOtherEvent | NewOtherEvent> = Omit<T, 'eventDate'> & { eventDate?: string | null };
type RestOtherEvent = RestOf<IOtherEvent>;

@Injectable({ providedIn: 'root' })
export class OtherEventService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/other-events');

  create(entity: NewOtherEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return this.http
      .post<RestOtherEvent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(entity: IOtherEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entity);
    return this.http
      .put<RestOtherEvent>(`${this.resourceUrl}/${entity.id}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOtherEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(): Observable<EntityArrayResponseType> {
    return this.http
      .get<RestOtherEvent[]>(this.resourceUrl, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient<T extends IOtherEvent | NewOtherEvent>(entity: T): RestOf<T> {
    return {
      ...(entity as any),
      eventDate: entity.eventDate ? entity.eventDate.format(DATE_FORMAT) : null,
    } as RestOf<T>;
  }

  protected convertDateFromServer(rest: RestOtherEvent): IOtherEvent {
    return {
      ...(rest as any),
      eventDate: rest.eventDate ? dayjs(rest.eventDate) : undefined,
    } as IOtherEvent;
  }

  protected convertResponseFromServer(res: HttpResponse<RestOtherEvent>): HttpResponse<IOtherEvent> {
    return res.clone({ body: res.body ? this.convertDateFromServer(res.body) : null });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOtherEvent[]>): HttpResponse<IOtherEvent[]> {
    return res.clone({ body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null });
  }
}
