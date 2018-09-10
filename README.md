Evaluation results service v1.1
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

Описание ключевой конфигурации модуля
----------------------------------------
Настройки для проекта находятся в файле application.yml. Ниже приведены основные параметры:
1) spring.datasource - настройки БД для хранения информации
2) webServiceConfig - настройки конфигурации веб - сервиса
    * webServiceConfig.wsdlConfig.portTypeName - список операций, которые могут быть выполнены с сообщениями
    * webServiceConfig.wsdlConfig.locationUri - url веб - сервиса
    * webServiceConfig.wsdlConfig.targetNamespace - целевое пространство имен схемы
    * webServiceConfig.wsdlConfig.xsdSchema: путь к xsd схеме
3) serviceConfig - основные настройки модуля
    * serviceConfig.dataStoragePath - путь к директории на файловой системе для хранения xml - файлов с
    обучающими выборками, которые использовались для определения основных показателей точности классификации
    * serviceConfig.fileFormat - формат xml - файлы с обучающей выборкой

Инструкция по развертыванию
----------------------------------------
    
1. В файловой системе создать директорию, например, с именем evaluationResultsStorage.

2. В настройке serviceConfig.dataStoragePath указать абсолютный путь к этой директории,
   например, /home/roman/evaluationResultsStorage.
   
3. Собрать проект с помощью системы сборки проекта maven. Ниже приведен пример команды:

   mvn clean install
   
4. Развернуть target/evaluation-results-service.war на одном из контейнеров сервлетов (например, Tomcat)
   с префиксом /evaluation-results-service.
   
Инструкция по развертыванию в Docker
-------------------------------------------------------

1. Для Windows достаточно скачать и установить дистрибутив Docker Desktop (https://www.docker.com/products/docker-desktop).

2. Далее, необходимо собрать проект с помощью команды:

mvn clean install dockerfile:build -Dspring.datasource.url=jdbc:postgresql://ers-db:5432/evaluation_results_storage -Dspring.jpa.hibernate.ddl-auto=update -DserviceConfig.dataStoragePath=/home/evaluationResultsStorage

3. Используя пакетный менеджер docker-compose, создать docker контейнеры с помощью команды:

docker-compose up

ВАЖНО! Данную команду необходимо выполнять из корневой папки проекта.

   
