## Основная идея:
1. отдельная Maven-конфигурация (maven_standard_flow.run.xml) собирает проект и подготавливает локальную БД;
2. отдельная Application-конфигурация запускает API (ApiApplication.run.xml).
3. То есть сначала пуск maven_standard_flow.run.xml и лишь затем ApiApplication.run.xml
4. Заточено на пользователей ОС Windows, IntelliJ IDEA
4. В директории 4 файла:
   - local-development/maven_standard_flow.run.xml - конфигурация запуска Maven flow с использованием профиля windows-docker-db
   - local-development/ApiApplication.run.xml - конфигурация запуска API-application
   - local-development/docker-compose.yml - конфигурация запуска PostgreSQL в Docker (исп. во время Maven flow и оттуда подхватывает переменные окружения)
   - local-development/check-docker.ps1 - проверка доступности Docker и попытка запуска (исп. во время Maven flow )

## Важно
Профиль Maven: windows-docker-db предназначен только для локальной разработки на Windows.
Использует PowerShell-скрипт из директории `local-development`.
## Локальная база данных
docker-compose.yml использует Docker volume, т.е. данные БД не пропадают при обычном пересоздании/остановке контейнера.

## Local build flow (для Windows-разработчиков)
Во время сборки проекта (local-development/maven_standard_flow.run.xml) выполняется Maven flow: 
clean install -Pwindows-docker-db

В рамках этого flow:
1. проверяется доступность Docker:
   -     ОК, если Docker CLI+Compose+Engine уже запущен/установлен любыми другими (менее типовыми для Windows) способами
   -     однако если выше упомянутое не установлено/не запущено - попытка запуска всего выше названного через запуск Docker Desktop как самый типовой способ для ОС Windows
   -     Если Docker Desktop не установлен - ошибка
2. если Docker CLI+Compose+Engine доступны/запущены - запускается PostgreSQL через Docker Compose;
3. ожидается готовность БД;
4. выполняются Flyway-миграции;
5. генерируются jOOQ-классы;
6. собирается проект.

## Для разработчиков Linux/Unix/MacOS etc
(Официально не автоматизировано как это сделано для Windows-пользователей)
Что вам стоит знать о проекте: нужна локальная БД
Вероятно, вам понадобится файл .env, ручное поднятие PostgreSQL через Docker Compose
И лишь потом запуск maven clean install
Переменные окружения и секреты, которые могут понадобиться - указаны в local-development/maven_standard_flow.run.xml
Все переменные (в том числе секреты) - тестовые, сгенерированные специально для локальной разработки.
Просьба не добавлять в проект новые файлы конфигураций БД: используйте или локальную БД у себя на ПК, или docker-compose.yml

## Запуск API
После успешной сборки API запускается через IntelliJ IDEA run configuration: ApiApplication
Файл конфигурации: local-development/ApiApplication.run.xml
API модуль исп. spring-boot-docker-compose-зависимость для подхвата docker-compose.yml благодаря настройкам в application-local.yml