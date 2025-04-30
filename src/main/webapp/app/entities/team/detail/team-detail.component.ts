import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TeamService } from '../service/team.service';
import SharedModule from 'app/shared/shared.module';
import { ITeam } from '../team.model';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'jhi-team-detail',
  templateUrl: './team-detail.component.html',
  imports: [SharedModule, RouterModule], // Импортируем необходимые модули
})
export class TeamDetailComponent {
  // Инжектируем сервисы через inject()
  private teamService = inject(TeamService);
  private route = inject(ActivatedRoute);

  // Используем сигналы для реактивного состояния
  team = signal<ITeam | null>(null); // Сигнал для хранения данных команды
  isLoading = false; // Флаг загрузки данных

  constructor() {
    // При создании компонента загружаем данные
    this.loadTeamWithEmployeesAndUsers();
  }

  /**
   * Загружает данные команды вместе с сотрудниками
   */
  loadTeamWithEmployeesAndUsers(): void {
    // Получаем ID команды из параметров маршрута
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.isLoading = true; // Устанавливаем флаг загрузки

      // Вызываем сервис для получения данных
      this.teamService.findWithEmployeesAndUsers(+id).subscribe({
        next: response => {
          this.team.set(response.body); // Обновляем сигнал с полученными данными
          this.isLoading = false; // Сбрасываем флаг загрузки
        },
        error: () => {
          this.isLoading = false; // В случае ошибки также сбрасываем флаг
        },
      });
    }
  }

  /**
   * Возвращает на предыдущую страницу
   */
  previousState(): void {
    window.history.back(); // Используем History API для навигации назад
  }
}
