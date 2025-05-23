import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { combineLatest, Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { CalendarEvent, EventType } from './calendar-event.model';
import { ITask } from '../../entities/task/task.model';
import dayjs from 'dayjs/esm';

@Injectable({ providedIn: 'root' })
export class CalendarService {
  private resourceUrl = 'api/calendar-tasks/events';
  private tasksUrl = 'api/calendar-tasks';

  constructor(private http: HttpClient) {}

  getEvents(): Observable<CalendarEvent[]> {
    return this.http.get<CalendarEvent[]>(this.resourceUrl).pipe(
      map(response => (Array.isArray(response) ? response : [])),
      catchError(error => {
        console.error('Error loading events:', error);
        return of([]);
      }),
    );
  }

  getTasksAsEvents(): Observable<CalendarEvent[]> {
    return this.http.get<any>(this.tasksUrl).pipe(
      map(response => {
        // Преобразуем ответ в массив, если это необходимо
        const tasks = Array.isArray(response) ? response : [];
        return tasks.map(task => this.convertTaskToEvent(task));
      }),
      catchError(error => {
        console.error('Error loading tasks:', error);
        return of([]);
      }),
    );
  }

  private convertTaskToEvent(task: ITask): CalendarEvent {
    try {
      let dateStr = '';
      const plannedDate = task.plannedCompletionDate;

      if (plannedDate) {
        const dateObj = typeof plannedDate === 'string' ? dayjs(plannedDate) : plannedDate;

        dateStr = dateObj.isValid() ? dateObj.format('YYYY-MM-DD') : '';
      }

      return {
        id: task.id,
        title: task.taskName || 'Без названия',
        description: task.body || '',
        date: dateStr,
        type: EventType.TASK,
        priority: task.priority || 'MEDIUM',
        status: task.status || 'UNKNOWN',
        startTime: '09:00',
        endTime: '10:00',
      };
    } catch (e) {
      console.error('Error converting task to event:', e);
      return {
        id: task.id,
        title: task.taskName || 'Без названия',
        description: task.body || '',
        date: '',
        type: EventType.TASK,
        priority: task.priority || 'MEDIUM',
        status: task.status || 'UNKNOWN',
      };
    }
  }
}
