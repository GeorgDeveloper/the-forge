// calendar.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CalendarEvent } from './calendar-event.model';

@Injectable({ providedIn: 'root' })
export class CalendarService {
  private resourceUrl = 'api/calendar/events';

  constructor(private http: HttpClient) {}

  /**
   * Получает все события календаря
   */
  getEvents(): Observable<CalendarEvent[]> {
    return this.http.get<CalendarEvent[]>(this.resourceUrl);
  }

  /**
   * Добавляет новое событие
   * @param event Событие для добавления
   */
  addEvent(event: CalendarEvent): Observable<CalendarEvent> {
    return this.http.post<CalendarEvent>(this.resourceUrl, event);
  }

  /**
   * Получает события для указанной даты
   * @param date Дата для фильтрации
   */
  getEventsByDate(date: Date): Observable<CalendarEvent[]> {
    const dateStr = date.toISOString().split('T')[0];
    return this.http.get<CalendarEvent[]>(`${this.resourceUrl}/date/${dateStr}`);
  }

  /**
   * Удаляет событие
   * @param id ID события
   */
  deleteEvent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.resourceUrl}/${id}`);
  }
}
