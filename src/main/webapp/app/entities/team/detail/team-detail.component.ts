import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TeamService } from '../service/team.service';
import SharedModule from 'app/shared/shared.module';
import { ITeam } from '../team.model';
import { IUser } from 'app/entities/user/user.model';
import { RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TeamUserDeleteDialogComponent } from './team-user-delete-dialog.component';

@Component({
  selector: 'jhi-team-detail',
  templateUrl: './team-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TeamDetailComponent {
  private teamService = inject(TeamService);
  private route = inject(ActivatedRoute);
  private modalService = inject(NgbModal);

  team = signal<ITeam | null>(null);
  isLoading = false;

  constructor() {
    this.loadTeamWithEmployeesAndUsers();
  }

  loadTeamWithEmployeesAndUsers(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.isLoading = true;
      this.teamService.findWithEmployeesAndUsers(+id).subscribe({
        next: response => {
          this.team.set(response.body);
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
        },
      });
    }
  }

  previousState(): void {
    window.history.back();
  }

  trackId(_index: number, item: IUser): number {
    return item.id!;
  }

  deleteUserFromTeam(user: IUser): void {
    const modalRef = this.modalService.open(TeamUserDeleteDialogComponent, {
      size: 'lg',
      backdrop: 'static',
      ariaLabelledBy: 'modal-basic-title',
    });

    modalRef.componentInstance.user = user;
    modalRef.componentInstance.team = this.team();

    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadTeamWithEmployeesAndUsers();
      }
    });
  }
}

// import { Component, inject, signal } from '@angular/core';
// import { ActivatedRoute } from '@angular/router';
// import { TeamService } from '../service/team.service';
// import SharedModule from 'app/shared/shared.module';
// import { ITeam } from '../team.model';
// import { RouterModule } from '@angular/router';
//
// @Component({
//   selector: 'jhi-team-detail',
//   templateUrl: './team-detail.component.html',
//   imports: [SharedModule, RouterModule], // Импортируем необходимые модули
// })
// export class TeamDetailComponent {
//   // Инжектируем сервисы через inject()
//   private teamService = inject(TeamService);
//   private route = inject(ActivatedRoute);
//
//   // Используем сигналы для реактивного состояния
//   team = signal<ITeam | null>(null); // Сигнал для хранения данных команды
//   isLoading = false; // Флаг загрузки данных
//
//   constructor() {
//     // При создании компонента загружаем данные
//     this.loadTeamWithEmployeesAndUsers();
//   }
//
//   /**
//    * Загружает данные команды вместе с сотрудниками
//    */
//   loadTeamWithEmployeesAndUsers(): void {
//     // Получаем ID команды из параметров маршрута
//     const id = this.route.snapshot.paramMap.get('id');
//
//     if (id) {
//       this.isLoading = true; // Устанавливаем флаг загрузки
//
//       // Вызываем сервис для получения данных
//       this.teamService.findWithEmployeesAndUsers(+id).subscribe({
//         next: response => {
//           this.team.set(response.body); // Обновляем сигнал с полученными данными
//           this.isLoading = false; // Сбрасываем флаг загрузки
//         },
//         error: () => {
//           this.isLoading = false; // В случае ошибки также сбрасываем флаг
//         },
//       });
//     }
//   }
//
//   /**
//    * Возвращает на предыдущую страницу
//    */
//   previousState(): void {
//     window.history.back(); // Используем History API для навигации назад
//   }
// }
