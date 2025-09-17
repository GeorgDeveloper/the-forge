import { Component, OnDestroy, OnInit, inject, signal, computed } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import 'dayjs/locale/ru';
import 'dayjs/locale/en';
import isBetween from 'dayjs/esm/plugin/isBetween';
import isSameOrAfter from 'dayjs/esm/plugin/isSameOrAfter';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { SafetyInstructionService } from 'app/entities/safety-instruction/service/safety-instruction.service';
import { TaskService } from 'app/entities/task/service/task.service';
import { CalendarService } from 'app/entities/calendar/calendar.service';
import { TeamService } from 'app/entities/team/service/team.service';
import { TrainingService } from 'app/entities/training/service/training.service';
import { MeetingService } from 'app/entities/meeting/service/meeting.service';
import { TranslateService } from '@ngx-translate/core';
import { ISafetyInstruction } from 'app/entities/safety-instruction/safety-instruction.model';
import { ITraining } from 'app/entities/training/training.model';
import { ITask } from 'app/entities/task/task.model';
import { ITeam } from 'app/entities/team/team.model';
import { IMeeting } from 'app/entities/meeting/meeting.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account = signal<Account | null>(null);
  // Widget data
  upcomingInstructions = signal<{ title: string; dateLabel: string }[]>([]);
  priorityTasks = signal<{ title: string; assigneeLabel: string; priorityKey: string; cssClass: string }[]>([]);
  calendarEvents = signal<{ day: string; title: string }[]>([]);
  // Loading flags
  isLoadingWidgets = signal<boolean>(false);
  activeEmployeesCount = signal<number>(0);
  activeTasksCount = signal<number>(0);
  requiresAttentionCount = signal<number>(0);
  // Screen-fit limits
  maxTrainings = signal<number>(3);
  maxTasks = signal<number>(3);
  maxMeetings = signal<number>(3);

  private readonly destroy$ = new Subject<void>();

  private readonly accountService = inject(AccountService);
  private readonly router = inject(Router);
  private readonly safetyInstructionService = inject(SafetyInstructionService);
  private readonly trainingService = inject(TrainingService);
  private readonly taskService = inject(TaskService);
  private readonly meetingService = inject(MeetingService);
  private readonly teamService = inject(TeamService);
  private readonly calendarService = inject(CalendarService);
  private readonly translateService = inject(TranslateService);

  ngOnInit(): void {
    dayjs.extend(isBetween);
    dayjs.extend(isSameOrAfter);
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account.set(account);
        this.loadTeamsForEmployeeCount();
      });

    this.loadWidgets();
    this.recalculateMaxItems();
    window.addEventListener('resize', this.recalculateMaxItems);
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  calendarMonth = computed(() => {
    // Use current language for month label
    const currentLang = this.translateService.currentLang || this.translateService.getDefaultLang() || 'ru';
    dayjs.locale(currentLang);
    return dayjs().format('MMMM YYYY');
  });

  private loadWidgets(): void {
    this.isLoadingWidgets.set(true);

    // Load upcoming trainings for current week (calendar-visible)
    this.trainingService
      .query({ sort: ['nextTrainingDate,asc'], size: 100 })
      .pipe(takeUntil(this.destroy$))
      .subscribe(response => {
        const list = (response.body || []) as ITraining[];
        const start = dayjs().startOf('day');
        const end = dayjs().endOf('week');
        const formatted = list
          .filter(t => t.nextTrainingDate && dayjs(t.nextTrainingDate as any).isBetween(start, end, null, '[]'))
          .sort((a, b) => (dayjs(a.nextTrainingDate as any).isAfter(dayjs(b.nextTrainingDate as any)) ? 1 : -1))
          .map(t => ({ title: t.trainingName || '', dateLabel: dayjs(t.nextTrainingDate as any).format('LL LT') }));
        // For widget list (today only)
        const todayList = list
          .filter(t => t.nextTrainingDate && dayjs(t.nextTrainingDate as any).isBetween(start, start.endOf('day'), null, '[]'))
          .sort((a, b) => (dayjs(a.nextTrainingDate as any).isAfter(dayjs(b.nextTrainingDate as any)) ? 1 : -1))
          .map(t => ({ title: t.trainingName || '', dateLabel: dayjs(t.nextTrainingDate as any).format('LT') }));
        this.upcomingInstructions.set(todayList);
        // Count for week (displayed in stats via length of filtered in week)
        // Reuse requiresAttentionCount for tasks, so nothing here; stats for upcoming will read from filteredWeekCount
        this._upcomingWeekCount = formatted.length;
      });

    // Load priority tasks (High/Medium)
    this.taskService
      .query({ sort: ['priority,desc', 'plannedCompletionDate,asc'], size: 20 })
      .pipe(takeUntil(this.destroy$))
      .subscribe(response => {
        const tasks = (response.body || []) as ITask[];
        this.activeTasksCount.set(tasks.filter(t => !t.actualCompletionDate && (t.status ?? 'TODO') !== 'DONE').length);
        const top = tasks
          .filter(t => !!t.priority && (t.status ?? 'TODO') !== 'DONE' && this.isInCurrentWeek(t.plannedCompletionDate))
          .sort((a, b) => (a.priority === 'HIGH' ? -1 : b.priority === 'HIGH' ? 1 : 0))
          .map(t => ({
            title: t.taskName || '',
            assigneeLabel: t.employee
              ? `${this.translateService.instant('task.assignedTo')}: ${`${t.employee.firstName || ''} ${t.employee.lastName || ''}`.trim()}`
              : this.translateService.instant('task.assignedUnknown'),
            priorityKey: t.priority || 'MEDIUM',
            cssClass: (t.priority || 'MEDIUM').toLowerCase(),
          }));
        this.priorityTasks.set(top);
        this.requiresAttentionCount.set(
          tasks.filter(
            t =>
              (t.status ?? 'TODO') !== 'DONE' &&
              !!t.plannedCompletionDate &&
              dayjs(t.plannedCompletionDate as any).isBefore(dayjs().startOf('day')),
          ).length,
        );
      });

    // Load meetings (upcoming)
    this.meetingService
      .query()
      .pipe(takeUntil(this.destroy$))
      .subscribe(response => {
        const meetings = (response.body || []) as IMeeting[];
        const today = dayjs().startOf('day');
        const formatted = meetings
          .filter(m => m.eventDate && dayjs(m.eventDate as any).isSameOrAfter(today))
          .sort((a, b) => (dayjs(a.eventDate as any).isAfter(dayjs(b.eventDate as any)) ? 1 : -1))
          .map(m => ({ day: dayjs(m.eventDate as any).format('D'), title: m.title || '' }));
        this.calendarEvents.set(formatted);
        this.isLoadingWidgets.set(false);
      });
  }

  private _upcomingWeekCount = 0;

  private loadTeamsForEmployeeCount(): void {
    const currentLogin = this.account()?.login;
    if (!currentLogin) {
      this.activeEmployeesCount.set(0);
      return;
    }
    this.teamService
      .query({ size: 1000 })
      .pipe(takeUntil(this.destroy$))
      .subscribe(res => {
        const teams = (res.body || []) as ITeam[];
        const myTeams = teams.filter(t => (t.users || []).some(u => u.login === currentLogin));
        const count = myTeams.reduce((sum, t) => sum + (t.employees || []).length, 0);
        this.activeEmployeesCount.set(count);
      });
  }

  private isInCurrentWeek(date?: dayjs.Dayjs | null): boolean {
    if (!date) return false;
    const d = dayjs(date as any);
    return d.isBetween(dayjs().startOf('week'), dayjs().endOf('week'), null, '[]');
  }

  private recalculateMaxItems = (): void => {
    // Estimate based on viewport height; each item ~72px height + paddings
    const vh = window.innerHeight || 800;
    const base = Math.max(1, Math.floor((vh - 480) / 72));
    this.maxTrainings.set(Math.max(1, base));
    this.maxTasks.set(Math.max(1, base));
    this.maxMeetings.set(Math.max(1, base));
  };

  upcomingWeekCount(): number {
    return this._upcomingWeekCount;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
