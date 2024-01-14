### Task - Hibernate

* Проект разработан на основе Spring Framework. В нем используются конфигурационные классы ApplicationConfig.java,
* DatabaseConfig.java, DispatcherInitializer.java, HibernateConfig.java, JdbcConfig.java, LiquibaseConfig.java и 
* WebMvcConfig.java для настройки контекста приложения. В проекте предусмотрена MVC архитектура. Для пояснения функциональность
* оформлены javadoc.
* Согласно условиям задания в проекте создано 2 сущности House и Person.
* House:
1. Содержит поля id, uuid, area, country, city, street, number, create_date.
2. Может иметь множество жильцов(0-n), что устанавливается полем residents.
3. У House может быть множество владельцев(0-n), что устанавливается полем owners.
4. create_date устанавливается один раз при создании с помощью HouseListener.
* Person:
1. Содержит поля id, uuid, name, surname, sex, passport, create_date, update_date. Поле passport является вложенной
* сущностью, которая содержит требуемые заданием поля passport_series, passport_number.
2. Выполнено требование, согласно которому Person обязан жить в одном доме и не может быть бездомным (поле house).
3. Person может владеть множеством домов, что устанавливается полем ownedHouses.
4. Обеспечивается требование уникальности полей passport_series и passport_number.
5. Поле sex имеет два значения Male или Female.
6. Все связи между таблицами обеспечены посредством id.
7. Не возвращаются id пользователю сервисом, возвращается uuid.
8. create_date устанавливается один раз при создании с помощью PersonListener.
9. update_date устанавливается при создании и изменяется каждый раз, когда меняется информация о Person. При этом, если 
* запрос не изменяет информации, поле не должно обновляться. Это обеспечено также с помощью PersonListener.
* Проект предоставляет REST API для выполнения следующих операций согласно требованиям задания:
*    a) CRUD для House
*        - в Get запросах не выводится информация о Person.
*    б) CRUD для Person
*        - в Get запросах не выводится информация о House(выводится только uuid).
*    в) для Get запросов используется pagination (по умолчанию 15). Пагинация осуществлена посредствам функционала Query.
* В проекте используется конфигурационный файл типа .yml. Создано 2 файла application-dev.yml и application-prod.yml с целью 
* обеспечить 2 типа конфигурации в проекте dev и prod. Выбор осуществляется указанием -Dspring.profiles.active=prod в 
* Edit Configurations -> VM Options.
* Скрипты для создания таблиц расположены в resources/db.changelog. Добавлена миграция баз данных посредствам Liquibase.
* В проекте JSON используется в качестве формата сообщений связи клиент-сервер.
* Реализован удобный механизм обработки ошибок/исключений. В проекте обработаны ошибки со статус кодами 404 и 500.
````json
[
  {
    "errorMessage": "Request method 'POST' is not supported",
    "errorCode": "500 INTERNAL_SERVER_ERROR"
  },
  {
    "errorMessage": "Person with 9aa78d35-fb66-45a6-8570-0f81513ef827 not found!",
    "errorCode": "404 NOT_FOUND"
  }
]
````
* В проекте согласно требованиям условия, о необходимости использовать репозитория с JdbcTemplate, я включил сюда также 
* следующие задания:
*   1. Get запрос для всех Person проживающих в House.
*   2. Get запрос для всех House, владельцем которых является Person.
*   3. Полнотекстовый поиск (любое текстовое поле) для House.
*   4. Полнотекстовый поиск (любое текстовое поле) для Person.
* Мной создан репозиторий GetDao, в котором реализована функциональность 4-х методов согласно заданию c использованием 
* JdbcTemplate и RowMappers для House и Person. Следующий промежуточный слой обработки реализован в JdbcService, а методы 
* для запросов реализованы в HouseController и PersonController. Соглашусь что название репозитория ещё нужно продумать.
* При реализации методов сохранения объектов House и Person, пришёл к тому что при добавлении новых объектов в списки 
* residents и owners House и список ownedHouses Person есть проблема создания новых сущностей, так как мы не используем 
* id, а uuid создаётся в при сохранении в базу данных. Как это решить пока не знаю.
* Пока не реализовал последнее задание ** PATH для Person и House.

### Технологии применённые в проекте

* Java 17
* Gradle 8.1.1
* Jakarta.validation-api:3.1.0-M1
* Slf4j 2.0.11
* Liquibase 4.25.1
* Fasterxml.jackson 2.14.2
* Spring-orm 6.1.2
* Spring-webmvc 6.1.2
* Hibernate-core 6.4.1.Final
* HicariCP 5.0.1
* Mapstruct 1.5.3.Final
* Postgresql 42.6.0
* Jakarta.servlet-api 6.0.0
* Spring-test:6.1.2
* H2database:h2 2.2.224
* Assertj-core 3.24.2
* Mockito-junit-jupiter 5.8.0

### Инструкция по запуску приложения локально

1. У вас должно быть установлено [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html),
   [Intellij IDEA Ultimate](https://www.jetbrains.com/idea/download/), [Tomcat 10.1](https://tomcat.apache.org/download-10.cgi)
   and [Postgresql](https://www.postgresql.org/download/).
2. В Postgresql вы должны создать базу данных houses.
3. В зависимости от используемого профиля вы вводите в application.yml введите свои username и password для своей локальной 
БД в строках №5, №6
4. В настройках Intellij IDEA, Run -> Edit Configurations... вы должны установить Tomcat 10 и указать в VM Options тип 
конфигурации -Dspring.profiles.active=prod или dev. 
5. Скрипты по созданию таблиц и загрузке данных выполняются автоматически посредствам Liquibase.
6. Приложение готово к работе.

### Модульные тесты

1. Модульные тесты были написаны со 100% охватом сервисов.
2. H2 используется для интеграционных тестов слоя Dao. Hibernate по умолчанию создает таблицы и заполняет их данными.
3. Интеграционные тесты для dao также имеют 100% покрытие.
4. Вы можете запустить тесты для этого проекта, выполнив в корне проекта:
```
./gradlew test
```

### Функциональные возможности

#### HouseController

* **GET findById | Находит один House в базе данных по uuid**
* http://localhost:8080/houses/0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6
* Response example:
````json
{
    "uuid": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
    "area": "Гомельская",
    "country": "Беларусь",
    "city": "Ельск",
    "street": "Ленина",
    "number": 2,
    "createDate": "2024-01-09T12:00:00:000"
}
````
* Bad Request example:
````json
{
    "errorMessage": "House with 0699cfd2-9fb7-4483-bcdf-194a2c6b7fe9 not found!",
    "errorCode": "404 NOT_FOUND"
}
````

* **GET findAll | Находит все House в базе данных согласно параметрам пагинации**
* http://localhost:8080/houses?pageNumber=1&pageSize=2
* Response example:
````json
[
   {
      "uuid": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
      "area": "Гомельская",
      "country": "Беларусь",
      "city": "Ельск",
      "street": "Ленина",
      "number": 2,
      "createDate": "2024-01-09T12:00:00:000"
   },
   {
      "uuid": "9724b9b8-216d-4ab9-92eb-e6e06029580d",
      "area": "Гомельская",
      "country": "Беларусь",
      "city": "Мозырь",
      "street": "Геологов",
      "number": 15,
      "createDate": "1998-08-12T12:00:00:000"
   }
]
````

* **GET findHousesWhichOwnPerson | Находит все House в базе данных согласно personUUID**
* http://localhost:8080/houses/owns/9aa78d35-fb66-45a6-8570-f81513ef8272
* Response example:
````json
[
    {
        "uuid": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
        "area": "Гомельская",
        "country": "Беларусь",
        "city": "Ельск",
        "street": "Ленина",
        "number": 2,
        "createDate": "2024-01-09T12:00:00:000"
    }
]
````

* **GET findHousesFullTextSearch | Находит все House в базе данных согласно searchTerm**
* http://localhost:8080/houses/fullsearch/ви
* Response example:
````json
[
    {
        "uuid": "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13",
        "area": "Гомельская",
        "country": "Беларусь",
        "city": "Калинковичи",
        "street": "Советская",
        "number": 8,
        "createDate": "2005-11-15T12:00:00:000"
    },
    {
        "uuid": "d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14",
        "area": "Минская",
        "country": "Беларусь",
        "city": "Смиловичи",
        "street": "Победы",
        "number": 12,
        "createDate": "1985-06-06T12:00:00:000"
    }
]
````

* **POST save | Сохраняет один House**
* http://localhost:8080/houses
* Request example:
````json
{
  "area": "Могилевская",
  "country": "Беларусь",
  "city": "Бобруйск",
  "street": "Минская",
  "number": 19
}
````
* Response example:
````json
{
    "uuid": "29e57309-1424-49bc-96a0-16ee41129102",
    "area": "Могилевская",
    "country": "Беларусь",
    "city": "Бобруйск",
    "street": "Минская",
    "number": 19,
    "createDate": "2024-01-14T22:09:57:823"
}
````

* **PUT update | Обновляет один House в базе данных по uuid**
* http://localhost:8080/houses/c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13
* Request example:
````json
{
  "area": "Витебская",
  "country": "Беларусь",
  "city": "Орша",
  "street": "Минская",
  "number": 10
}
````
* Response example:
````json
{
  "uuid": "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13",
  "area": "Витебская",
  "country": "Беларусь",
  "city": "Орша",
  "street": "Минская",
  "number": 10,
  "createDate": "2005-11-15T12:00:00:000"
}
````

* **DELETE delete | Удаляет один House в базе данных по uuid**
* http://localhost:8080/houses/e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15

#### PersonController

* **GET findById | Находит один Persone в базе данных по uuid**
* http://localhost:8080/persons/9aa78d35-fb66-45a6-8570-f81513ef8272
* Response example:
````json
{
   "uuid": "9aa78d35-fb66-45a6-8570-f81513ef8272",
   "name": "Марина",
   "surname": "Громкая",
   "sex": "FEMALE",
   "passport": {
      "series": "HB",
      "number": "123456"
   },
   "houseUUID": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
   "createDate": "2022-03-06T10:00:00:000",
   "updateDate": "2022-03-06T10:00:00:000"
}
````
* Bad Request example:
````json
{
   "errorMessage": "Person with 9aa78d35-fb66-45a6-8570-f81513ef8277 not found!",
   "errorCode": "404 NOT_FOUND"
}
````

* **GET findAll | Находит все Person в базе данных согласно параметрам пагинации**
* http://localhost:8080/persons?pageNumber=1&pageSize=3
* Response example:
````json
[
   {
      "uuid": "9aa78d35-fb66-45a6-8570-f81513ef8272",
      "name": "Марина",
      "surname": "Громкая",
      "sex": "FEMALE",
      "passport": {
         "series": "HB",
         "number": "123456"
      },
      "houseUUID": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
      "createDate": "2022-03-06T10:00:00:000",
      "updateDate": "2022-03-06T10:00:00:000"
   },
   {
      "uuid": "922e0213-e543-48ef-b8cb-92592afd5100",
      "name": "Иван",
      "surname": "Рогозин",
      "sex": "MALE",
      "passport": {
         "series": "HM",
         "number": "234567"
      },
      "houseUUID": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
      "createDate": "2022-01-07T16:00:00:000",
      "updateDate": "2022-01-07T16:00:00:000"
   },
   {
      "uuid": "00cc3880-2e7a-47a8-b688-91e9565e972d",
      "name": "Женя",
      "surname": "Древко",
      "sex": "MALE",
      "passport": {
         "series": "WE",
         "number": "345678"
      },
      "houseUUID": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
      "createDate": "2022-09-08T17:00:00:000",
      "updateDate": "2022-09-08T17:00:00:000"
   }
]
````

* **GET findPersonWhichLiveInHouse | Находит всеx Person в базе данных согласно houseUUID**
* http://localhost:8080/persons/lives/0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6
* Response example:
````json
[
   {
      "uuid": "9aa78d35-fb66-45a6-8570-f81513ef8272",
      "name": "Марина",
      "surname": "Громкая",
      "sex": "FEMALE",
      "passport": {
         "series": "HB",
         "number": "123456"
      },
      "houseUUID": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
      "createDate": "2022-03-06T10:00:00:000",
      "updateDate": "2022-03-06T10:00:00:000"
   },
   {
      "uuid": "922e0213-e543-48ef-b8cb-92592afd5100",
      "name": "Иван",
      "surname": "Рогозин",
      "sex": "MALE",
      "passport": {
         "series": "HM",
         "number": "234567"
      },
      "houseUUID": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
      "createDate": "2022-01-07T16:00:00:000",
      "updateDate": "2022-01-07T16:00:00:000"
   },
   {
      "uuid": "00cc3880-2e7a-47a8-b688-91e9565e972d",
      "name": "Женя",
      "surname": "Древко",
      "sex": "MALE",
      "passport": {
         "series": "WE",
         "number": "345678"
      },
      "houseUUID": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
      "createDate": "2022-09-08T17:00:00:000",
      "updateDate": "2022-09-08T17:00:00:000"
   }
]
````

* **GET findPersonsFullTextSearch | Находит все Person в базе данных согласно searchTerm**
* http://localhost:8080/persons/fullsearch/ар
* В данном ответе возвращается null из-за недостающей функциональности PersonRowMapper,
потому что необходимо добавить способ вытянуть House по id указанному в таблице Persons. 
Эту проблему я знаю, знаю где возникает и устраню.
* Response example:
````json
[
   {
      "uuid": "9aa78d35-fb66-45a6-8570-f81513ef8272",
      "name": "Марина",
      "surname": "Громкая",
      "sex": "FEMALE",
      "passport": {
         "series": "HB",
         "number": "123456"
      },
      "houseUUID": null,
      "createDate": "2022-03-06T10:00:00:000",
      "updateDate": "2022-03-06T10:00:00:000"
   }
]
````

* **POST save | Сохраняет один Person**
* http://localhost:8080/persons
* Request example:
````json
{
   "name": "Михаил",
   "surname": "Цалко",
   "sex": "MALE",
   "passport": {
      "series": "TY",
      "number": "654456"
   },
   "houseUUID": "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13",
   "ownedHouses": [
      {
         "uuid": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
         "area": "Гомельская",
         "country": "Беларусь",
         "city": "Ельск",
         "street": "Ленина",
         "number": 2
      }
   ]
}
````
* Response example:
````json
{
   "uuid": "e48e63ce-d0ef-4db2-bae5-f8b8cd43f5e1",
   "name": "Михаил",
   "surname": "Цалко",
   "sex": "MALE",
   "passport": {
      "series": "TY",
      "number": "654456"
   },
   "houseUUID": "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13",
   "createDate": "2024-01-14T23:31:17:972",
   "updateDate": "2024-01-14T23:31:17:972"
}
````

* **PUT update | Обновляет один Person в базе данных по uuid**
* http://localhost:8080/persons/3df38f0a-09bb-4bbc-a80c-2f827b6f9d75
* Request example:
````json
{
   "name": "Иван",
   "surname": "Мележ",
   "sex": "MALE",
   "passport": {
      "series": "BL",
      "number": "654379"
   },
   "houseUUID": "9724b9b8-216d-4ab9-92eb-e6e06029580d"
}
````
* Response example:
````json
{
   "uuid": "3df38f0a-09bb-4bbc-a80c-2f827b6f9d75",
   "name": "Иван",
   "surname": "Мележ",
   "sex": "MALE",
   "passport": {
      "series": "BL",
      "number": "654379"
   },
   "houseUUID": "9724b9b8-216d-4ab9-92eb-e6e06029580d",
   "createDate": "2022-04-13T22:00:00:000",
   "updateDate": "2024-01-14T22:32:36:259"
}
````

* **DELETE delete | Удаляет один Person в базе данных по uuid**
* http://localhost:8080/persons/6cb850ef-298a-4e0b-9c46-bf3c527cd017