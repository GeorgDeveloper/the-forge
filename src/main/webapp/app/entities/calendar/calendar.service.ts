import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { combineLatest, Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { CalendarEvent, EventType } from './calendar-event.model';
import { ITask } from '../../entities/task/task.model';
import { ITraining } from '../../entities/training/training.model';
import { IAdditionalTraining } from '../../entities/additional-training/additional-training.model';
import { ISafetyInstruction } from '../../entities/safety-instruction/safety-instruction.model';
import { TrainingService } from '../../entities/training/service/training.service';
import { AdditionalTrainingService } from '../../entities/additional-training/service/additional-training.service';
import { SafetyInstructionService } from '../../entities/safety-instruction/service/safety-instruction.service';
import dayjs from 'dayjs/esm';

@Injectable({ providedIn: 'root' })
export class CalendarService {
  private resourceUrl = 'api/calendar-tasks/events';
  private tasksUrl = 'api/calendar-tasks';

  constructor(
    private http: HttpClient,
    private trainingService: TrainingService,
    private additionalTrainingService: AdditionalTrainingService,
    private safetyInstructionService: SafetyInstructionService,
  ) {}

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

  // Получение инструктажей как событий календаря
  getTrainingsAsEvents(): Observable<CalendarEvent[]> {
    return this.trainingService.query().pipe(
      map(response => {
        const trainings = response.body || [];
        return trainings.filter(training => training.nextTrainingDate).map(training => this.convertTrainingToEvent(training));
      }),
      catchError(error => {
        console.error('Error loading trainings:', error);
        return of([]);
      }),
    );
  }

  // Получение дополнительных инструктажей как событий календаря
  getAdditionalTrainingsAsEvents(): Observable<CalendarEvent[]> {
    return this.additionalTrainingService.query().pipe(
      map(response => {
        const additionalTrainings = response.body || [];
        return additionalTrainings
          .filter(training => training.nextTrainingDate)
          .map(training => this.convertAdditionalTrainingToEvent(training));
      }),
      catchError(error => {
        console.error('Error loading additional trainings:', error);
        return of([]);
      }),
    );
  }

  // Получение инструктажей по безопасности как событий календаря
  getSafetyInstructionsAsEvents(): Observable<CalendarEvent[]> {
    return this.safetyInstructionService.query().pipe(
      map(response => {
        const safetyInstructions = response.body || [];
        return safetyInstructions
          .filter(instruction => instruction.introductionDate)
          .map(instruction => this.convertSafetyInstructionToEvent(instruction));
      }),
      catchError(error => {
        console.error('Error loading safety instructions:', error);
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

  private convertTrainingToEvent(training: ITraining): CalendarEvent {
    try {
      let dateStr = '';
      if (training.nextTrainingDate) {
        const dateObj = typeof training.nextTrainingDate === 'string' ? dayjs(training.nextTrainingDate) : training.nextTrainingDate;
        dateStr = dateObj.isValid() ? dateObj.format('YYYY-MM-DD') : '';
      }

      const employeeName = training.employee
        ? `${training.employee.firstName || ''} ${training.employee.lastName || ''}`.trim()
        : 'Неизвестный сотрудник';

      return {
        id: training.id,
        title: `Инструктаж: ${training.trainingName || 'Без названия'}`,
        description: `Сотрудник: ${employeeName}`,
        date: dateStr,
        type: EventType.INSTRUCTION,
        startTime: '09:00',
        endTime: '10:00',
        participants: [employeeName],
        employeeId: training.employee?.id,
        employeeName: employeeName,
        validityPeriod: training.validityPeriod || undefined,
      };
    } catch (e) {
      console.error('Error converting training to event:', e);
      return {
        id: training.id,
        title: `Инструктаж: ${training.trainingName || 'Без названия'}`,
        description: 'Ошибка загрузки данных',
        date: '',
        type: EventType.INSTRUCTION,
      };
    }
  }

  private convertAdditionalTrainingToEvent(training: IAdditionalTraining): CalendarEvent {
    try {
      let dateStr = '';
      if (training.nextTrainingDate) {
        const dateObj = typeof training.nextTrainingDate === 'string' ? dayjs(training.nextTrainingDate) : training.nextTrainingDate;
        dateStr = dateObj.isValid() ? dateObj.format('YYYY-MM-DD') : '';
      }

      const professionName = training.profession?.professionName || 'Неизвестная профессия';

      return {
        id: training.id,
        title: `Доп. инструктаж: ${training.trainingName || 'Без названия'}`,
        description: `Профессия: ${professionName}`,
        date: dateStr,
        type: EventType.INSTRUCTION,
        startTime: '09:00',
        endTime: '10:00',
        participants: [professionName],
        professionId: training.profession?.id,
        professionName: professionName,
        validityPeriod: training.validityPeriod || undefined,
      };
    } catch (e) {
      console.error('Error converting additional training to event:', e);
      return {
        id: training.id,
        title: `Доп. инструктаж: ${training.trainingName || 'Без названия'}`,
        description: 'Ошибка загрузки данных',
        date: '',
        type: EventType.INSTRUCTION,
      };
    }
  }

  private convertSafetyInstructionToEvent(instruction: ISafetyInstruction): CalendarEvent {
    try {
      let dateStr = '';
      if (instruction.introductionDate) {
        const dateObj =
          typeof instruction.introductionDate === 'string' ? dayjs(instruction.introductionDate) : instruction.introductionDate;
        dateStr = dateObj.isValid() ? dateObj.format('YYYY-MM-DD') : '';
      }

      const professionName = instruction.profession?.professionName || 'Неизвестная профессия';
      const positionName = instruction.position?.positionName || 'Неизвестная должность';

      return {
        id: instruction.id,
        title: `Инструктаж по ТБ: ${instruction.instructionName || 'Без названия'}`,
        description: `Профессия: ${professionName}, Должность: ${positionName}`,
        date: dateStr,
        type: EventType.INSTRUCTION,
        startTime: '09:00',
        endTime: '10:00',
        participants: [professionName, positionName],
        professionId: instruction.profession?.id,
        professionName: professionName,
        positionId: instruction.position?.id,
        positionName: positionName,
      };
    } catch (e) {
      console.error('Error converting safety instruction to event:', e);
      return {
        id: instruction.id,
        title: `Инструктаж по ТБ: ${instruction.instructionName || 'Без названия'}`,
        description: 'Ошибка загрузки данных',
        date: '',
        type: EventType.INSTRUCTION,
      };
    }
  }
}
