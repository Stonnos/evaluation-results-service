Evaluation results service v1.2
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
    * web-service-config.wsdlConfig.portTypeName - список операций, которые могут быть выполнены с сообщениями
    * web-service-config.wsdlConfig.locationUri - url веб - сервиса
    * web-service-config.wsdlConfig.targetNamespace - целевое пространство имен схемы
    * web-service-config.wsdlConfig.xsdSchema: путь к xsd схеме
3) service-config - основные настройки модуля
    * service-config.resultSize - число наилучших конфигураций классификаторов

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

docker-compose up (для ОС семейства Linux)

docker-compose -f docker-compose.yaml -f docker-compose.win10.yaml up (для Windows 10)

ВАЖНО! Данную команду необходимо выполнять из корневой папки проекта.
