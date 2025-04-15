// Импорт необходимых Angular модулей и сервисов
import { Component, inject } from '@angular/core';
import { registerLocaleData } from '@angular/common'; // Для регистрации локали
import dayjs from 'dayjs/esm'; // Библиотека для работы с датами
import { FaIconLibrary } from '@fortawesome/angular-fontawesome'; // Сервис для работы с иконками FontAwesome
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap'; // Конфигурация datepicker из ng-bootstrap
import locale from '@angular/common/locales/ru'; // Локаль для русского языка

// Импорт сервисов и компонентов приложения
import { ApplicationConfigService } from 'app/core/config/application-config.service'; // Сервис конфигурации
import { fontAwesomeIcons } from './config/font-awesome-icons'; // Набор используемых иконок
import MainComponent from './layouts/main/main.component'; // Главный компонент-обертка

// Декоратор компонента
@Component({
  selector: 'jhi-app', // Селектор компонента (префикс jhi - JHipster convention)
  template: '<jhi-main></jhi-main>', // Шаблон - просто рендерит главный компонент
  imports: [
    MainComponent, // Импорт главного компонента
  ],
})
export default class AppComponent {
  // Внедрение зависимостей через inject()
  private readonly applicationConfigService = inject(ApplicationConfigService);
  private readonly iconLibrary = inject(FaIconLibrary);
  private readonly dpConfig = inject(NgbDatepickerConfig);

  constructor() {
    // Установка префикса для API endpoints
    this.applicationConfigService.setEndpointPrefix(SERVER_API_URL);

    // Регистрация русской локали для Angular (форматы дат, чисел и т.д.)
    registerLocaleData(locale);

    // Добавление иконок FontAwesome в библиотеку
    this.iconLibrary.addIcons(...fontAwesomeIcons);

    // Настройка минимальной даты в datepicker (100 лет назад от текущей даты)
    this.dpConfig.minDate = {
      year: dayjs().subtract(100, 'year').year(),
      month: 1,
      day: 1,
    };
  }
}
