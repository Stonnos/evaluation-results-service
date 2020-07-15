Evaluation results service v1.4
========================================

Описание
----------------------------------------
   Evaluation results service представляет собой SOAP веб - сервис, который предназначен для хранения
и анализа результатов классификации разнотипных данных с использованием различных алгоритмов классификации,
как одиночных так и ансамблевых. Сервис также предоставляет API для нахождения оптимальных параметров
классификаторов для конкретной обучающей выборки на основе накопленной истории результатов классификации.
Наилучшие конфигурации классификаторов выбираются по критерию максимизации точности классификатора.


Необходимый софт
----------------------------------------
* jdk 1.8
* maven >= 3.3.9
* База данных PostgreSQL для хранения информации.
* Tomcat >= 8

Описание ключевой конфигурации модуля
----------------------------------------
Настройки для проекта находятся в файле application.yml. Ниже приведены основные параметры:
1) spring.datasource - настройки БД для хранения информации
2) web-service-config - настройки конфигурации веб - сервиса
    * web-service-config.xsdSchema: путь к xsd схеме
3) service-config - основные настройки модуля
    * service-config.resultSize - число наилучших конфигураций классификаторов

Бизнес метрики приложения
----------------------------------------
* ers.operation.save-evaluation-results.timed.seconds.max - Макс. время выполнения операции сохранения результатов классификации
* ers.operation.save-evaluation-results.timed.seconds.count - Общее число операций сохранения результатов классификации
* ers.operation.save-evaluation-results.timed.seconds.sum - Суммарное время для операций сохранения результатов классификации
* ers.operation.get-evaluation-results.timed.seconds.max - Макс. время выполнения операции получения результатов классификации
* ers.operation.get-evaluation-results.timed.seconds.count - Общее число операций получения результатов классификации
* ers.operation.get-evaluation-results.timed.seconds.sum - Суммарное время для операций получения результатов классификации
* ers.operation.get-optimal-classifier-options.timed.seconds.max - Макс. время выполнения операции поиска оптимальных конфигураций классификаторов
* ers.operation.get-optimal-classifier-options.seconds.count - Общее число операций поиска оптимальных конфигураций классификаторов
* ers.operation.get-optimal-classifier-options.timed.seconds.sum - Суммарное время для операций поиска оптимальных конфигураций классификаторов

Метрики приложения доступны по адресу http://[service.host]:[service.port]/actuator/prometheus

Инструкция по развертыванию
----------------------------------------
   
1. Собрать проект с помощью системы сборки проекта maven. Ниже приведен пример команды:

   mvn clean install
   
2. Развернуть target/evaluation-results-service.war на одном из контейнеров сервлетов (например, Tomcat 8)
   с префиксом /evaluation-results-service.
   
Инструкция по развертыванию в Docker
-------------------------------------------------------

1. Для Windows 10 достаточно скачать и установить дистрибутив Docker Desktop (https://www.docker.com/products/docker-desktop).

   Для Linux сначала необходимо установить Docker CE (https://docs.docker.com/install/linux/docker-ce/ubuntu/),
   затем Docker compose (https://docs.docker.com/compose/install/).

2. Далее для сборки проекта и создания образа проекта нужно выполнить команду

    mvn clean install dockerfile:build

3. Используя пакетный менеджер docker-compose, создать docker контейнеры с помощью команды:

    docker-compose up -d

ВАЖНО! Данную команду необходимо выполнять из корневой папки проекта.
