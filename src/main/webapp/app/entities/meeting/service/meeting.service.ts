import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IMeeting, NewMeeting } from '../meeting.model';

export type EntityResponseType = HttpResponse<IMeeting>;
export type EntityArrayResponseType = HttpResponse<IMeeting[]>;

type RestOf<T extends IMeeting | NewMeeting> = Omit<T, 'eventDate'> & { eventDate?: string | null };
type RestMeeting = RestOf<IMeeting>;

@Injectable({ providedIn: 'root' })
export class MeetingService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meetings');

  create(meeting: NewMeeting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meeting);
    return this.http
      .post<RestMeeting>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(meeting: IMeeting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meeting);
    return this.http
      .put<RestMeeting>(`${this.resourceUrl}/${meeting.id}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMeeting>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(): Observable<EntityArrayResponseType> {
    return this.http
      .get<RestMeeting[]>(this.resourceUrl, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient<T extends IMeeting | NewMeeting>(meeting: T): RestOf<T> {
    return {
      ...(meeting as any),
      eventDate: meeting.eventDate ? meeting.eventDate.format(DATE_FORMAT) : null,
    } as RestOf<T>;
  }

  protected convertDateFromServer(rest: RestMeeting): IMeeting {
    return {
      ...(rest as any),
      eventDate: rest.eventDate ? dayjs(rest.eventDate) : undefined,
    } as IMeeting;
  }

  protected convertResponseFromServer(res: HttpResponse<RestMeeting>): HttpResponse<IMeeting> {
    return res.clone({ body: res.body ? this.convertDateFromServer(res.body) : null });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMeeting[]>): HttpResponse<IMeeting[]> {
    return res.clone({ body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null });
  }
}
