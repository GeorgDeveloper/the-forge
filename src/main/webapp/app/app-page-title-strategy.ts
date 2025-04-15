// Импорт необходимых декораторов и сервисов из Angular и сторонних библиотек
import { Injectable, inject } from '@angular/core';
import { RouterStateSnapshot, TitleStrategy } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

// Декоратор @Injectable указывает, что этот класс может быть внедрен как зависимость
@Injectable()
// Наследуемся от базового класса TitleStrategy для реализации кастомной стратегии заголовков
export class AppPageTitleStrategy extends TitleStrategy {
  // Внедряем сервис для перевода (i18n)
  private readonly translateService = inject(TranslateService);

  // Переопределяем метод updateTitle базового класса
  override updateTitle(routerState: RouterStateSnapshot): void {
    // Получаем заголовок страницы, используя метод buildTitle из родительского класса
    let pageTitle = this.buildTitle(routerState);

    // Если заголовок не задан в маршруте, используем заголовок по умолчанию
    if (!pageTitle) {
      pageTitle = 'global.title'; // Ключ для перевода заголовка по умолчанию
    }

    // Используем сервис перевода для получения локализованного заголовка
    this.translateService.get(pageTitle).subscribe(title => {
      // Устанавливаем полученный перевод в качестве title страницы
      document.title = title;
    });
  }
}
