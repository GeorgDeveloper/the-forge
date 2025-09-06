import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITeam } from '../team.model';
import { TeamService } from '../service/team.service';
import { TeamFormGroup, TeamFormService } from './team-form.service';
import { IEmployee } from 'app/entities/employee/employee.model'; // Импорт интерфейса IEmployee для работы с данными о сотрудниках
import { IUser } from 'app/entities/user/user.model'; // Импорт интерфейса IUser для работы с данными о пользователях
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { UserService } from '../../user/service/user.service'; // Импорт сервиса EmployeeService для получения данных о сотрудниках
import { IOrganization } from '../../organization/organization.model';
import { OrganizationService } from '../../organization/service/organization.service';

@Component({
  selector: 'jhi-team-update', // Селектор компонента, используется для вставки компонента в HTML
  templateUrl: './team-update.component.html', // Путь к HTML-шаблону компонента
  imports: [SharedModule, FormsModule, ReactiveFormsModule], // Импорт необходимых модулей Angular
})
export class TeamUpdateComponent implements OnInit {
  isSaving = false; // Флаг, указывающий, выполняется ли сохранение данных
  team: ITeam | null = null; // Объект команды, который мы редактируем или создаем, может быть null
  employees: IEmployee[] = []; // Массив всех доступных сотрудников для выбора
  users: IUser[] = []; // Массив всех доступных пользователей для выбора
  organizations: IOrganization[] = [];

  protected teamService = inject(TeamService); // Внедрение сервиса TeamService для работы с данными команд
  protected teamFormService = inject(TeamFormService); // Внедрение сервиса TeamFormService для работы с формой команды
  protected activatedRoute = inject(ActivatedRoute); // Внедрение сервиса ActivatedRoute для получения данных из URL
  protected employeeService = inject(EmployeeService); // Внедрение сервиса EmployeeService для работы с данными сотрудников
  protected userService = inject(UserService); // Внедрение сервиса UserService для работы с данными пользователей
  protected organizationService = inject(OrganizationService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TeamFormGroup = this.teamFormService.createTeamFormGroup(); // Создание формы для редактирования команды с помощью TeamFormService

  ngOnInit(): void {
    // Загружаем все необходимые данные параллельно
    this.loadAllEmployees(); // Вызов метода для загрузки всех сотрудников при инициализации компонента
    this.loadAllUsers();
    this.loadOrganizations();

    this.activatedRoute.data.subscribe(({ team }) => {
      // Подписка на данные, переданные через маршрут (например, данные команды для редактирования)
      this.team = team;
      if (team) {
        // Небольшая задержка, чтобы организации успели загрузиться
        setTimeout(() => {
          this.updateForm(team); // Если команда существует, заполняем форму данными команды
        }, 100);
      }
    });
  }

  loadOrganizations(): void {
    this.organizationService.query().subscribe({ next: res => (this.organizations = res.body ?? []) });
  }

  loadAllEmployees(): void {
    // Метод для загрузки всех сотрудников из сервиса EmployeeService
    this.employeeService.query().subscribe({
      next: (res: HttpResponse<IEmployee[]>) => {
        // При успешном получении данных, сохраняем массив сотрудников в свойство employees
        this.employees = res.body ?? []; // Используем оператор ?? для установки пустого массива, если res.body равен null или undefined
      },
      error: () => console.error('Error loading employees'), // Обработка ошибки при загрузке сотрудников
    });
  }

  loadAllUsers(): void {
    // Метод для загрузки всех сотрудников из сервиса UserService
    this.userService.query().subscribe({
      next: (res: HttpResponse<IUser[]>) => {
        // При успешном получении данных, сохраняем массив сотрудников в свойство users
        this.users = res.body ?? []; // Используем оператор ?? для установки пустого массива, если res.body равен null или undefined
      },
      error: () => console.error('Error loading users'), // Обработка ошибки при загрузке сотрудников
    });
  }

  previousState(): void {
    // Метод для возврата к предыдущему состоянию (например, к списку команд)
    window.history.back();
  }

  save(): void {
    // Метод для сохранения данных команды
    this.isSaving = true; // Устанавливаем флаг сохранения в true
    const team = this.teamFormService.getTeam(this.editForm); // Получаем данные команды из формы
    if (team.id !== null) {
      // Если у команды есть id, значит, это редактирование существующей команды
      this.subscribeToSaveResponse(this.teamService.update(team)); // Вызываем метод для обновления команды
    } else {
      // Если у команды нет id, значит, это создание новой команды
      this.subscribeToSaveResponse(this.teamService.create(team)); // Вызываем метод для создания команды
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeam>>): void {
    // Метод для обработки ответа от сервиса при сохранении команды
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      // Используем оператор finalize для выполнения действий после завершения запроса (независимо от успеха или ошибки)
      next: () => this.onSaveSuccess(), // Обработка успешного сохранения
      error: () => this.onSaveError(), // Обработка ошибки при сохранении
    });
  }

  protected onSaveSuccess(): void {
    // Метод для обработки успешного сохранения команды
    this.previousState(); // Возвращаемся к предыдущему состоянию
  }

  protected onSaveError(): void {
    // Метод для обработки ошибки при сохранении команды (можно добавить логику обработки ошибок)
  }

  protected onSaveFinalize(): void {
    // Метод для выполнения действий после завершения запроса на сохранение (например, сброс флага isSaving)
    this.isSaving = false;
  }

  protected updateForm(team: ITeam): void {
    // Метод для заполнения формы данными команды
    this.team = team;
    this.teamFormService.resetForm(this.editForm, team); // Сброс и заполнение формы данными команды

    // Устанавливаем выбранных сотрудников
    if (team.employees) {
      this.editForm.patchValue({
        employees: team.employees, // Установка выбранных сотрудников в форму
      });
    }

    if (team.users) {
      this.editForm.patchValue({
        users: team.users, // Установка выбранных сотрудников в форму
      });
    }

    // Устанавливаем выбранную организацию
    if (team.organization) {
      this.editForm.patchValue({
        organization: team.organization, // Установка выбранной организации в форму
      });
    }
  }

  isEmployeeSelected(employee: IEmployee): boolean {
    // Метод для проверки, выбран ли сотрудник в команде
    if (!this.team || !this.team.employees) return false; // Если команда или сотрудники не существуют, возвращаем false
    return this.team.employees.some(e => e.id === employee.id); // Проверяем, есть ли сотрудник в списке сотрудников команды
  }

  isUsersSelected(user: IUser): boolean {
    // Метод для проверки, выбран ли сотрудник в команде
    if (!this.team || !this.team.users) return false; // Если команда или сотрудники не существуют, возвращаем false
    return this.team.users.some(e => e.id === user.id); // Проверяем, есть ли сотрудник в списке сотрудников команды
  }
}
