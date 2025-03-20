# myTeamsJhipster

Это приложение было создано с использованием JHipster 8.9.0. Документацию и помощь можно найти по адресу [https://www.jhipster.tech/documentation-archive/v8.9.0](https://www.jhipster.tech/documentation-archive/v8.9.0).

## Структура проекта

Для генерации проекта требуется Node, а также он рекомендуется для разработки. Файл `package.json` всегда создается для улучшения опыта разработки с использованием prettier, хуков для коммитов, скриптов и т.д.

В корне проекта JHipster генерирует конфигурационные файлы для таких инструментов, как git, prettier, eslint, husky и других, которые хорошо известны, и вы можете найти ссылки на них в интернете.

Структура `/src/*` соответствует стандартной структуре Java.

- `.yo-rc.json` - Конфигурационный файл Yeoman  
  Конфигурация JHipster хранится в этом файле по ключу `generator-jhipster`. Вы можете найти `generator-jhipster-*` для конфигурации специфичных blueprints.
- `.yo-resolve` (опционально) - Решатель конфликтов Yeoman  
  Позволяет использовать определенное действие при обнаружении конфликтов, пропуская запросы для файлов, соответствующих шаблону. Каждая строка должна соответствовать шаблону `[pattern] [action]`, где pattern — это [Minimatch](https://github.com/isaacs/minimatch#minimatch) шаблон, а action — одно из значений: skip (по умолчанию, если опущено) или force. Строки, начинающиеся с `#`, считаются комментариями и игнорируются.
- `.jhipster/*.json` - Конфигурационные файлы сущностей JHipster

- `npmw` - обёртка для использования локально установленного npm.  
  JHipster по умолчанию устанавливает Node и npm локально с использованием инструмента сборки. Эта обёртка гарантирует, что npm установлен локально и использует его, избегая различий, которые могут вызвать разные версии. Используя `./npmw` вместо традиционного `npm`, вы можете настроить среду без Node для разработки или тестирования вашего приложения.
- `/src/main/docker` - Docker-конфигурации для приложения и сервисов, от которых зависит приложение.

## Разработка

Система сборки автоматически установит рекомендуемую версию Node и npm.

Мы предоставляем обертку для запуска npm.
Вам нужно будет запускать эту команду только при изменении зависимостей в файле [package.json](package.json).

```
./npmw install
```

Мы используем npm-скрипты и [Angular CLI][] вместе с [Webpack][] в качестве нашей системы сборки.

Запустите следующие команды в двух отдельных терминалах, чтобы создать комфортную среду разработки, где ваш браузер
автоматически обновляется при изменении файлов на вашем жестком диске.

```
./gradlew -x webapp
./npmw start
```

Npm также используется для управления зависимостями CSS и JavaScript, используемыми в этом приложении. Вы можете обновить зависимости, указав
более новую версию в файле [package.json](package.json). Вы также можете запустить `./npmw update` и `./npmw install` для управления зависимостями.
Добавьте флаг `help` к любой команде, чтобы увидеть, как ее можно использовать. Например, `./npmw help update`.

Команда `./npmw run` выведет список всех доступных скриптов для запуска в этом проекте.

### Поддержка PWA

JHipster предоставляет поддержку PWA (Progressive Web App), которая по умолчанию отключена. Одним из основных компонентов PWA является сервис-воркер.

Код инициализации сервис-воркера отключен по умолчанию. Чтобы включить его, раскомментируйте следующий код в файле `src/main/webapp/app/app.config.ts`:

```typescript
ServiceWorkerModule.register('ngsw-worker.js', { enabled: false });
```

### Управление зависимостями

Например, чтобы добавить библиотеку [Leaflet][] как runtime-зависимость вашего приложения, выполните следующую команду:

```
./npmw install --save --save-exact leaflet
```

Чтобы воспользоваться TypeScript-типами из репозитория [DefinitelyTyped][] в разработке, выполните следующую команду:

```
./npmw install --save-dev --save-exact @types/leaflet
```

Затем импортируйте JS и CSS файлы, указанные в инструкции по установке библиотеки, чтобы [Webpack][] знал о них:
Отредактируйте файл [src/main/webapp/app/app.config.ts](src/main/webapp/app/app.config.ts):

```
import 'leaflet/dist/leaflet.js';
```

Отредактируйте файл [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss):

```
@import 'leaflet/dist/leaflet.css';
```

Примечание: Есть еще несколько вещей, которые нужно сделать для Leaflet, но мы не будем их здесь детализировать.

Для получения дополнительных инструкций по разработке с использованием JHipster, ознакомьтесь с [Using JHipster in development][].

### Использование Angular CLI

Вы также можете использовать [Angular CLI][] для генерации пользовательского клиентского кода.

Например, следующая команда:

```
ng generate component my-component
```

создаст несколько файлов:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.config.ts
```

## Сборка для production

### Упаковка в jar

Чтобы собрать финальный jar-файл и оптимизировать приложение myTeamsJhipster для production, выполните:

```
./gradlew -Pprod clean bootJar
```

Эта команда объединит и минифицирует CSS и JavaScript файлы клиентской части. Также она изменит `index.html`, чтобы он ссылался на эти новые файлы.
Чтобы убедиться, что всё работает, выполните:

```
java -jar build/libs/*.jar
```

Затем перейдите в браузере по адресу [http://localhost:8098](http://localhost:8098).

Для получения дополнительной информации обратитесь к [Using JHipster in production][].

### Упаковка в war

Чтобы упаковать ваше приложение в war-файл для развертывания на сервере приложений, выполните:

```
./gradlew -Pprod -Pwar clean bootWar
```

### JHipster Control Center

JHipster Control Center помогает управлять и контролировать ваши приложения. Вы можете запустить локальный сервер Control Center (доступный по адресу http://localhost:7419) с помощью команды:

```
docker compose -f src/main/docker/jhipster-control-center.yml up
```

## Тестирование

### Тесты Spring Boot

Для запуска тестов вашего приложения выполните:

```
./gradlew test integrationTest jacocoTestReport
```

### Клиентские тесты

Модульные тесты выполняются с помощью [Jest][]. Они находятся рядом с компонентами и могут быть запущены командой:

```
./npmw test
```

## Дополнительно

### Анализ качества кода с помощью Sonar

Sonar используется для анализа качества кода. Вы можете запустить локальный сервер Sonar (доступный по адресу http://localhost:9001) с помощью команды:

```
docker compose -f src/main/docker/sonar.yml up -d
```

Примечание: мы отключили принудительную переадресацию аутентификации для UI в [src/main/docker/sonar.yml](src/main/docker/sonar.yml) для удобства использования SonarQube "из коробки". Для реальных сценариев использования рекомендуется включить её обратно.

Вы можете выполнить анализ Sonar с помощью [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) или используя плагин Gradle.

Затем выполните анализ Sonar:

```
./gradlew -Pprod clean check jacocoTestReport sonarqube -Dsonar.login=admin -Dsonar.password=admin
```

Кроме того, вместо передачи `sonar.password` и `sonar.login` через аргументы командной строки, эти параметры можно настроить в файле [sonar-project.properties](sonar-project.properties), как показано ниже:

```
sonar.login=admin
sonar.password=admin
```

Для получения дополнительной информации обратитесь к [странице анализа качества кода][].

### Поддержка Docker Compose

JHipster генерирует несколько файлов конфигурации Docker Compose в папке [src/main/docker/](src/main/docker/) для запуска необходимых сторонних сервисов.

Например, чтобы запустить необходимые сервисы в контейнерах Docker, выполните:

```
docker compose -f src/main/docker/services.yml up -d
```

Чтобы остановить и удалить контейнеры, выполните:

```
docker compose -f src/main/docker/services.yml down
```

[Интеграция Spring с Docker Compose](https://docs.spring.io/spring-boot/reference/features/dev-services.html) включена по умолчанию. Её можно отключить в файле application.yml:

```yaml
spring:
  docker:
    compose:
      enabled: false
```

Вы также можете полностью "докеризировать" ваше приложение и все зависящие от него сервисы.
Для этого сначала создайте Docker-образ вашего приложения, выполнив:

```sh
npm run java:docker
```

Или создайте Docker-образ для архитектуры arm64, если используете процессор на базе arm64, например, MacOS с процессором семейства M1

```sh
npm run java:docker:arm64
```

Затем выполните:

```sh
docker compose -f src/main/docker/app.yml up -d
```

Для получения дополнительной информации обратитесь к [странице использования Docker и Docker-Compose][], где также содержится информация о суб-генераторе Docker Compose (jhipster docker-compose), который может генерировать конфигурации Docker для одного или нескольких JHipster-приложений.

## Непрерывная интеграция (опционально)

Чтобы настроить CI для вашего проекта, запустите суб-генератор ci-cd (`jhipster ci-cd`). Это позволит вам сгенерировать конфигурационные файлы для ряда систем непрерывной интеграции. Подробнее см. на странице [Настройка непрерывной интеграции][].
[JHipster Homepage and latest documentation]: https://www.jhipster.tech
[JHipster 8.9.0 archive]: https://www.jhipster.tech/documentation-archive/v8.9.0
[Using JHipster in development]: https://www.jhipster.tech/documentation-archive/v8.9.0/development/
[Using Docker and Docker-Compose]: https://www.jhipster.tech/documentation-archive/v8.9.0/docker-compose
[Using JHipster in production]: https://www.jhipster.tech/documentation-archive/v8.9.0/production/
[Running tests page]: https://www.jhipster.tech/documentation-archive/v8.9.0/running-tests/
[Code quality page]: https://www.jhipster.tech/documentation-archive/v8.9.0/code-quality/
[Setting up Continuous Integration]: https://www.jhipster.tech/documentation-archive/v8.9.0/setting-up-ci/
[Node.js]: https://nodejs.org/
[NPM]: https://www.npmjs.com/
[Webpack]: https://webpack.github.io/
[BrowserSync]: https://www.browsersync.io/
[Jest]: https://jestjs.io
[Leaflet]: https://leafletjs.com/
[DefinitelyTyped]: https://definitelytyped.org/
[Angular CLI]: https://cli.angular.io/
