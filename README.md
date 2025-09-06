# The Forge - Система управления персоналом и задачами

**The Forge** - это веб-приложение для управления персоналом, задачами, обучением и безопасностью на производстве. Приложение создано с использованием JHipster 8.9.0 и включает в себя современный Angular frontend и Spring Boot backend.

## 🎯 Функциональность приложения

- **Управление сотрудниками** - ведение кадрового учета, должности, профессии
- **Система задач** - постановка, отслеживание и контроль выполнения задач
- **Обучение и инструктажи** - управление обязательными и дополнительными обучениями
- **Безопасность труда** - учет средств защиты и инструктажей по безопасности
- **Командная работа** - управление командами и доступом сотрудников
- **Календарь событий** - планирование и отслеживание важных дат
- **Отчетность** - аналитика и мониторинг процессов

## 🏗️ Технологический стек

- **Backend**: Spring Boot 3.x, Java 17, PostgreSQL
- **Frontend**: Angular 19, TypeScript, Bootstrap 5
- **База данных**: PostgreSQL 17.2
- **Сборка**: Gradle, Webpack
- **Контейнеризация**: Docker, Docker Compose
- **Мониторинг**: Spring Boot Actuator, Prometheus

Документацию и помощь можно найти по адресу [https://www.jhipster.tech/documentation-archive/v8.9.0](https://www.jhipster.tech/documentation-archive/v8.9.0).

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

## 🚀 Быстрый старт

### Предварительные требования

- **Java 17** или выше
- **Node.js 22.13.1** или выше
- **PostgreSQL 17.2** или выше
- **Docker** и **Docker Compose** (опционально)

### Установка зависимостей

Система сборки автоматически установит рекомендуемую версию Node и npm.

```bash
# Установка зависимостей (только при первом запуске или изменении package.json)
./npmw install
```

## 💻 Разработка

Мы используем npm-скрипты и [Angular CLI][] вместе с [Webpack][] в качестве нашей системы сборки.

### Запуск в режиме разработки

Запустите следующие команды в двух отдельных терминалах для создания комфортной среды разработки:

**Терминал 1 - Backend:**

```bash
./gradlew -x webapp
```

**Терминал 2 - Frontend:**

```bash
./npmw start
```

Приложение будет доступно по адресу: [http://localhost:8098](http://localhost:8098)

### Альтернативный запуск (все в одном терминале)

```bash
# Запуск backend и frontend одновременно
./npmw run watch
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

## 🏭 Сборка для production

### Сборка JAR файла (рекомендуется)

Для создания готового к деплою JAR файла выполните:

```bash
# Полная сборка с оптимизацией
./gradlew -Pprod clean bootJar

# Быстрая сборка без тестов
./gradlew -Pprod clean bootJar -x test -x integrationTest
```

Эта команда:

- Объединит и минифицирует CSS и JavaScript файлы клиентской части
- Скомпилирует и оптимизирует backend код
- Создаст единый исполняемый JAR файл

**Результат:** `build/libs/the-forge-0.0.1-SNAPSHOT.jar`

### Сборка WAR файла

Для развертывания на сервере приложений (Tomcat, JBoss и т.д.):

```bash
./gradlew -Pprod -Pwar clean bootWar
```

**Результат:** `build/libs/the-forge-0.0.1-SNAPSHOT.war`

### Сборка Docker образа

```bash
# Создание Docker образа
npm run java:docker:prod

# Для ARM64 архитектуры (Apple M1/M2)
npm run java:docker:arm64
```

## 🖥️ Развертывание на сервере

### Вариант 1: JAR файл (рекомендуется)

#### 1. Подготовка сервера

**Требования к серверу:**

- Java 17 или выше
- PostgreSQL 17.2 или выше
- Минимум 2GB RAM
- 10GB свободного места

#### 2. Установка PostgreSQL

**Ubuntu/Debian:**

```bash
# Установка PostgreSQL
sudo apt update
sudo apt install postgresql postgresql-contrib

# Создание базы данных и пользователя
sudo -u postgres psql
CREATE DATABASE theForge;
CREATE USER theForge WITH PASSWORD 'theForge';
GRANT ALL PRIVILEGES ON DATABASE theForge TO theForge;
\q
```

**CentOS/RHEL:**

```bash
# Установка PostgreSQL
sudo yum install postgresql-server postgresql-contrib
sudo postgresql-setup initdb
sudo systemctl enable postgresql
sudo systemctl start postgresql

# Создание базы данных
sudo -u postgres psql
CREATE DATABASE theForge;
CREATE USER theForge WITH PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE theForge TO theForge;
\q
```

#### 3. Настройка приложения

Создайте файл конфигурации `application-prod.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/theForge
    username: theForge
    password: your_secure_password
  jpa:
    hibernate:
      ddl-auto: update
  liquibase:
    contexts: prod

jhipster:
  mail:
    base-url: https://your-domain.com
  security:
    authentication:
      jwt:
        base64-secret: your_base64_secret_key_here

server:
  port: 8098
```

#### 4. Запуск приложения

```bash
# Загрузка JAR файла на сервер
scp build/libs/the-forge-0.0.1-SNAPSHOT.jar user@server:/opt/theforge/

# Запуск приложения
java -jar the-forge-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

# Запуск в фоновом режиме с логированием
nohup java -jar the-forge-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod > app.log 2>&1 &

# Запуск как systemd сервис
sudo systemctl start theforge
```

#### 5. Настройка systemd сервиса

Создайте файл `/etc/systemd/system/theforge.service`:

```ini
[Unit]
Description=The Forge Application
After=postgresql.service

[Service]
Type=simple
User=theforge
WorkingDirectory=/opt/theforge
ExecStart=/usr/bin/java -jar the-forge-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
# Активация сервиса
sudo systemctl daemon-reload
sudo systemctl enable theforge
sudo systemctl start theforge
```

### Вариант 2: Docker Compose (рекомендуется для контейнеризации)

#### 1. Подготовка Docker Compose файла

Создайте `docker-compose.prod.yml`:

```yaml
version: '3.8'
services:
  postgresql:
    image: postgres:17.2
    environment:
      - POSTGRES_DB=theForge
      - POSTGRES_USER=theForge
      - POSTGRES_PASSWORD=your_secure_password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - '5432:5432'
    restart: unless-stopped

  app:
    image: theforge:latest
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgresql:5432/theForge
      - SPRING_DATASOURCE_USERNAME=theForge
      - SPRING_DATASOURCE_PASSWORD=your_secure_password
    ports:
      - '8098:8098'
    depends_on:
      - postgresql
    restart: unless-stopped

volumes:
  postgres_data:
```

#### 2. Запуск с Docker Compose

```bash
# Сборка и запуск
docker-compose -f docker-compose.prod.yml up -d

# Просмотр логов
docker-compose -f docker-compose.prod.yml logs -f

# Остановка
docker-compose -f docker-compose.prod.yml down
```

### Вариант 3: WAR файл на сервере приложений

#### Tomcat

```bash
# Копирование WAR файла
sudo cp build/libs/the-forge-0.0.1-SNAPSHOT.war /var/lib/tomcat9/webapps/theforge.war

# Перезапуск Tomcat
sudo systemctl restart tomcat9
```

Приложение будет доступно по адресу: `http://your-server:8080/theforge`

### Настройка Nginx (опционально)

Создайте конфигурацию `/etc/nginx/sites-available/theforge`:

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8098;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

```bash
# Активация конфигурации
sudo ln -s /etc/nginx/sites-available/theforge /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### Мониторинг и логи

```bash
# Просмотр логов приложения
tail -f /opt/theforge/app.log

# Проверка статуса сервиса
sudo systemctl status theforge

# Проверка здоровья приложения
curl http://localhost:8098/management/health

# Мониторинг метрик
curl http://localhost:8098/management/prometheus
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

## 🐳 Работа с Docker

### Локальная разработка с Docker

JHipster генерирует несколько файлов конфигурации Docker Compose в папке [src/main/docker/](src/main/docker/) для запуска необходимых сторонних сервисов.

#### Запуск только базы данных

```bash
# Запуск PostgreSQL в Docker
docker compose -f src/main/docker/postgresql.yml up -d

# Остановка
docker compose -f src/main/docker/postgresql.yml down
```

#### Запуск всех сервисов

```bash
# Запуск всех необходимых сервисов
docker compose -f src/main/docker/services.yml up -d

# Остановка
docker compose -f src/main/docker/services.yml down
```

#### Полная контейнеризация приложения

```bash
# Создание Docker образа приложения
npm run java:docker:prod

# Для ARM64 архитектуры (Apple M1/M2)
npm run java:docker:arm64

# Запуск приложения с базой данных
docker compose -f src/main/docker/app.yml up -d
```

### Отключение Docker Compose интеграции

[Интеграция Spring с Docker Compose](https://docs.spring.io/spring-boot/reference/features/dev-services.html) включена по умолчанию. Её можно отключить в файле application.yml:

```yaml
spring:
  docker:
    compose:
      enabled: false
```

## 🗄️ Работа с базой данных

### Настройка PostgreSQL

#### Локальная установка

**Windows:**

1. Скачайте PostgreSQL с [официального сайта](https://www.postgresql.org/download/windows/)
2. Установите с настройками по умолчанию
3. Создайте базу данных:

```sql
CREATE DATABASE theForge;
CREATE USER theForge WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE theForge TO theForge;
```

**macOS:**

```bash
# Установка через Homebrew
brew install postgresql
brew services start postgresql

# Создание базы данных
createdb theForge
psql theForge
CREATE USER theForge WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE theForge TO theForge;
```

**Linux (Ubuntu/Debian):**

```bash
# Установка
sudo apt update
sudo apt install postgresql postgresql-contrib

# Создание базы данных
sudo -u postgres psql
CREATE DATABASE theForge;
CREATE USER theForge WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE theForge TO theForge;
\q
```

#### Docker (рекомендуется для разработки)

```bash
# Запуск PostgreSQL в Docker
docker run --name postgres-theforge \
  -e POSTGRES_DB=theForge \
  -e POSTGRES_USER=theForge \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d postgres:17.2

# Подключение к базе данных
docker exec -it postgres-theforge psql -U theForge -d theForge
```

### Миграции базы данных

Приложение использует Liquibase для управления миграциями базы данных. Миграции автоматически выполняются при запуске приложения.

**Конфигурация миграций:**

- Файлы миграций: `src/main/resources/config/liquibase/`
- Главный файл: `src/main/resources/config/liquibase/master.xml`
- Контексты: `dev` (разработка), `prod` (production)

**Ручное выполнение миграций:**

```bash
# Выполнение миграций
./gradlew liquibaseUpdate

# Откат миграций
./gradlew liquibaseRollback

# Генерация новой миграции
./gradlew liquibaseDiffChangeLog
```

### Резервное копирование

```bash
# Создание резервной копии
pg_dump -h localhost -U theForge -d theForge > backup_$(date +%Y%m%d_%H%M%S).sql

# Восстановление из резервной копии
psql -h localhost -U theForge -d theForge < backup_20240120_120000.sql
```

### Мониторинг базы данных

```bash
# Подключение к базе данных
psql -h localhost -U theForge -d theForge

# Просмотр таблиц
\dt

# Просмотр размера базы данных
SELECT pg_size_pretty(pg_database_size('theForge'));

# Просмотр активных подключений
SELECT * FROM pg_stat_activity WHERE datname = 'theForge';
```

Для получения дополнительной информации обратитесь к [странице использования Docker и Docker-Compose][], где также содержится информация о суб-генераторе Docker Compose (jhipster docker-compose), который может генерировать конфигурации Docker для одного или нескольких JHipster-приложений.

## 🛠️ Полезные команды

### Разработка

```bash
# Установка зависимостей
./npmw install

# Запуск в режиме разработки
./gradlew -x webapp          # Backend
./npmw start                 # Frontend

# Запуск тестов
./gradlew test               # Backend тесты
./npmw test                  # Frontend тесты

# Линтинг и форматирование
./npmw lint                  # Проверка кода
./npmw lint:fix              # Автоисправление
./npmw prettier:format       # Форматирование кода

# Сборка
./gradlew bootJar            # JAR для разработки
./gradlew -Pprod bootJar     # JAR для production
```

### База данных

```bash
# Запуск PostgreSQL в Docker
docker compose -f src/main/docker/postgresql.yml up -d

# Выполнение миграций
./gradlew liquibaseUpdate

# Создание резервной копии
pg_dump -h localhost -U theForge -d theForge > backup.sql
```

### Docker

```bash
# Сборка образа
npm run java:docker:prod

# Запуск приложения с базой данных
docker compose -f src/main/docker/app.yml up -d

# Просмотр логов
docker compose -f src/main/docker/app.yml logs -f
```

### Мониторинг

```bash
# Проверка здоровья приложения
curl http://localhost:8098/management/health

# Просмотр метрик
curl http://localhost:8098/management/prometheus

# Просмотр информации о приложении
curl http://localhost:8098/management/info
```

## 🔧 Устранение неполадок

### Частые проблемы

**1. Ошибка подключения к базе данных:**

```bash
# Проверьте, запущена ли PostgreSQL
sudo systemctl status postgresql

# Проверьте подключение
psql -h localhost -U theForge -d theForge
```

**2. Порт 8098 уже занят:**

```bash
# Найдите процесс, использующий порт
sudo netstat -tulpn | grep :8098

# Остановите процесс
sudo kill -9 <PID>
```

**3. Ошибки сборки:**

```bash
# Очистка и пересборка
./gradlew clean
./gradlew bootJar

# Очистка npm кэша
./npmw cache clean --force
```

**4. Проблемы с Docker:**

```bash
# Очистка Docker
docker system prune -a

# Пересборка образов
docker-compose build --no-cache
```

### Логи и отладка

```bash
# Просмотр логов приложения
tail -f logs/the-forge.log

# Включение debug режима
java -jar the-forge-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod --debug

# Просмотр логов Docker
docker logs <container_name>
```

## 📚 Дополнительные ресурсы

- [JHipster Documentation](https://www.jhipster.tech/documentation-archive/v8.9.0/)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Angular Documentation](https://angular.io/docs)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

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
