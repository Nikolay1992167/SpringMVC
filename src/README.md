### Task - Spring Boot Tests

* За основу взят проект разработан на основе Spring Boot Framework. 
* Согласно условиям задания в проекте:
* 1) Покрытие тестами обеспечено на 83% согласно Coverage.
* 2) Для тестирования используется тестконтейнер. Использую PostgreSQLContainer.
* 3) Применён mockMvc.
* Реализованы тесты:
* 1) Для LFUCacheImpl и LRUCacheImpl.
* 2) Для HouseController и PersonController модульные тесты с использованием mockMvc. 
* 3) Для ControllerAdvice модульные тесты.
* 4) Для HouseRepository и PersonRepository интеграционные тесты с использованием тестконтейнера.
* 5) Для уровня сервисов написаны модульные тесты: HouseServiceImplTest и PersonServiceImplTest,
* интеграционные тесты: HouseServiceImplIT и PersonServiceImplIT, реализованы наверное модульные тесты с использованием 
* @MockBean репозиториев и подключением тест контейнеров, пока верю слову ментора, что в определённых ситуациях они
* нужны и их реализовал HouseServiceImplTestContainer и PersonServiceImplTestContainer.
* Вы можете запустить тесты для этого проекта, выполнив в корне проекта:
```
./gradlew test
```

### Технологии применённые в проекте

* Java 17
* Gradle 8.1.1
* Springframework.boot' version '3.2.1
* Spring-boot-starter-data-jpa
* Spring-boot-starter-web
* spring-boot-starter-validation
* Liquibase 4.25.1
* Jackson-databind 2.14.2
* Mapstruct 1.5.3.Final
* Postgresql 42.6.0
* Testcontainers:postgresql 1.19.3
* Spring-boot-starter-test
* Assertj-core 3.24.2
* Mockito-junit-jupiter 5.8.0
* Springdoc-openapi-starter-webmvc-ui 2.3.0

### Инструкция по запуску приложения локально

1. У вас должно быть установлено [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html),
   [Intellij IDEA Ultimate](https://www.jetbrains.com/idea/download/), [Tomcat 10.1](https://tomcat.apache.org/download-10.cgi)
   and [Postgresql](https://www.postgresql.org/download/).
2. В Postgresql вы должны создать базу данных houses.
3. В зависимости от используемого профиля вы вводите в application.yml укажите тип профиля spring: profiles: active:-.
Введите свои username и password в соответсвующий файл в строках №5, №6
4. Скрипты по созданию таблиц и загрузке данных выполняются автоматически посредствам Liquibase.
5. Приложение готово к работе.

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
   "createDate":"2023-12-30T12:00:00:000"
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
* http://localhost:8080/houses?page=0&size=2
* Response example:
````json
{
   "content": [
      {
         "uuid": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
         "area": "Гомельская",
         "country": "Беларусь",
         "city": "Ельск",
         "street": "Ленина",
         "number": 2,
         "createDate": "2023-12-30T12:00:00:000"
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
   ],
   "pageable": {
      "pageNumber": 0,
      "pageSize": 2,
      "sort": {
         "empty": true,
         "sorted": false,
         "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
   },
   "last": false,
   "totalPages": 4,
   "totalElements": 7,
   "size": 2,
   "number": 0,
   "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
   },
   "first": true,
   "numberOfElements": 2,
   "empty": false
   }
````

* **GET findHousesWhichSomeTimeLivesPerson | Находит все House в которых когда-либо жил Person with personUUID**
* http://localhost:8080/houses/sometimelives/922e0213-e543-48ef-b8cb-92592afd5100
* Response example:
````json
{
  "content": [
    {
      "uuid": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
      "area": "Гомельская",
      "country": "Беларусь",
      "city": "Ельск",
      "street": "Ленина",
      "number": 2,
      "createDate": "2023-12-30T12:00:00:000"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 1,
  "size": 15,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
````

* **GET findHousesWhichOwnPerson | Находит все House, которыми владеет Person with personUUID**
* http://localhost:8080/houses/owns/63a1faca-a963-4d4b-bfb9-2dafaedc36fe
* Response example:
````json
{
    "content": [
        {
            "uuid": "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15",
            "area": "Бресткая",
            "country": "Беларусь",
            "city": "Пинск",
            "street": "Весенняя",
            "number": 5,
            "createDate": "2003-03-18T12:00:00:000"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 15,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "size": 15,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
}
````

* **GET findHousesWhichSomeTimeOwnPerson | Находит все House, которыми когда-либо владел Person with personUUID**
* http://localhost:8080/houses/sometimeowns/863db796-cf16-4c67-ad24-710d0d2f0341
* Response example:
````json
{
    "content": [
        {
            "uuid": "d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14",
            "area": "Минская",
            "country": "Беларусь",
            "city": "Смиловичи",
            "street": "Победы",
            "number": 12,
            "createDate": "1985-06-06T12:00:00:000"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 15,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "size": 15,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
}
````

* **GET findHousesFullTextSearch | Находит все House в базе данных согласно searchTerm**
* http://localhost:8080/houses/fullsearch/ви
* Response example:
````json
{
  "content": [
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
    },
    {
      "uuid": "4b7d1a13-c0e9-4bb2-b280-a55cbc13ec37",
      "area": "Минская",
      "country": "Республика Беларусь",
      "city": "Минск",
      "street": "Независимости",
      "number": 3,
      "createDate": "2024-01-24T12:45:32:336"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 15,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 3,
  "size": 15,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 3,
  "empty": false
}
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
  "uuid": "3193f8d7-7501-4771-888b-6963977b4b55",
  "area": "Могилевская",
  "country": "Беларусь",
  "city": "Бобруйск",
  "street": "Минская",
  "number": 19,
  "createDate": "2024-01-25T17:50:45:565"
}
````

* **PUT update | Обновляет один House в базе данных по uuid**
* http://localhost:8080/houses/e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15
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
  "uuid": "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15",
  "area": "Витебская",
  "country": "Беларусь",
  "city": "Орша",
  "street": "Минская",
  "number": 10,
  "createDate": "2003-03-18T12:00:00:000"
}
````

* **PATCH update | Обновляет данные одного House в базе данных по uuid**
* http://localhost:8080/houses/9724b9b8-216d-4ab9-92eb-e6e06029580d
* Request example:
````json
{
   "street": "Северная",
   "number": 126
}
````
* Response example:
````json
{
   "uuid": "9724b9b8-216d-4ab9-92eb-e6e06029580d",
   "area": "Гомельская",
   "country": "Беларусь",
   "city": "Мозырь",
   "street": "Северная",
   "number": 126,
   "createDate": "1998-08-12T12:00:00:000"
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
* http://localhost:8080/persons?page=0&size=3
* Response example:
````json
{
  "content": [
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
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 3,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalPages": 4,
  "totalElements": 10,
  "size": 3,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 3,
  "empty": false
}
````

* **GET findPersonsWhichLiveInHouse | Находит всеx Person, которые живут в House c houseUUID**
* http://localhost:8080/persons/lives/d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14
* Response example:
````json
{
   "content": [
      {
         "uuid": "3df38f0a-09bb-4bbc-a80c-2f827b6f9d75",
         "name": "Игорь",
         "surname": "Гнедов",
         "sex": "MALE",
         "passport": {
            "series": "HP",
            "number": "890123"
         },
         "houseUUID": "d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14",
         "createDate": "2022-04-13T22:00:00:000",
         "updateDate": "2022-04-13T22:00:00:000"
      },
      {
         "uuid": "f188d7be-146b-4668-a729-09a2d4fdc784",
         "name": "Егор",
         "surname": "Калина",
         "sex": "MALE",
         "passport": {
            "series": "MN",
            "number": "789012"
         },
         "houseUUID": "d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14",
         "createDate": "2021-01-12T21:00:00:000",
         "updateDate": "2024-01-24T12:51:04:123"
      }
   ],
   "pageable": {
      "pageNumber": 0,
      "pageSize": 15,
      "sort": {
         "empty": true,
         "sorted": false,
         "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
   },
   "last": true,
   "totalPages": 1,
   "totalElements": 2,
   "size": 15,
   "number": 0,
   "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
   },
   "first": true,
   "numberOfElements": 2,
   "empty": false
}
````

* **GET findPersonsWhichSomeTimeLiveInHouse | Находит всеx Person, которые когда-либо проживали в House c houseUUID**
* http://localhost:8080/persons/sometimelives/e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15
* Response example:
````json
{
    "content": [
        {
            "uuid": "63a1faca-a963-4d4b-bfb9-2dafaedc36fe",
            "name": "Леня",
            "surname": "Дудич",
            "sex": "MALE",
            "passport": {
                "series": "HR",
                "number": "901234"
            },
            "houseUUID": "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15",
            "createDate": "2021-01-14T13:00:00:000",
            "updateDate": "2021-01-14T13:00:00:000"
        },
        {
            "uuid": "3df38f0a-09bb-4bbc-a80c-2f827b6f9d75",
            "name": "Игорь",
            "surname": "Гнедов",
            "sex": "MALE",
            "passport": {
                "series": "HP",
                "number": "890123"
            },
            "houseUUID": "d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14",
            "createDate": "2022-04-13T22:00:00:000",
            "updateDate": "2022-04-13T22:00:00:000"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 15,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 2,
    "size": 15,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
}
````

* **GET findPersonsWhichSomeTimeOwnHouse | Находит всеx Person, которые когда-либо владели House c houseUUID**
* http://localhost:8080/persons/sometimeownes/e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15
* Response example:
````json
{
    "content": [
        {
            "uuid": "3df38f0a-09bb-4bbc-a80c-2f827b6f9d75",
            "name": "Игорь",
            "surname": "Гнедов",
            "sex": "MALE",
            "passport": {
                "series": "HP",
                "number": "890123"
            },
            "houseUUID": "d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14",
            "createDate": "2022-04-13T22:00:00:000",
            "updateDate": "2022-04-13T22:00:00:000"
        },
        {
            "uuid": "63a1faca-a963-4d4b-bfb9-2dafaedc36fe",
            "name": "Леня",
            "surname": "Дудич",
            "sex": "MALE",
            "passport": {
                "series": "HR",
                "number": "901234"
            },
            "houseUUID": "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15",
            "createDate": "2021-01-14T13:00:00:000",
            "updateDate": "2021-01-14T13:00:00:000"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 15,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 2,
    "size": 15,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
}
````


* **GET findPersonsFullTextSearch | Находит все Person в базе данных согласно searchTerm**
* http://localhost:8080/persons/fullsearch/ар
* Response example:
````json
{
   "content": [
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
   ],
   "pageable": {
      "pageNumber": 0,
      "pageSize": 15,
      "sort": {
         "empty": true,
         "sorted": false,
         "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
   },
   "last": true,
   "totalPages": 1,
   "totalElements": 1,
   "size": 15,
   "number": 0,
   "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
   },
   "first": true,
   "numberOfElements": 1,
   "empty": false
}
````

* **POST save | Сохраняет один Person**
* http://localhost:8080/persons
* Request example:
````json
{
   "name": "Вася",
   "surname": "Дудкин",
   "sex": "MALE",
   "passport": {
      "series": "XB",
      "number": "333852"
   },
   "houseUUID": "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13",
   "ownedHouses": [
      {
         "uuid": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
         "area": "Минская",
         "country": "Республика Беларусь",
         "city": "Минск",
         "street": "Независимости",
         "number": 3
      },
      {
         "uuid": "123e4567-e89b-12d3-a456-426614174000",
         "area": "Могилёвская",
         "country": "Республика Белаурсь",
         "city": "Миоры",
         "street": "Тверская",
         "number": 2
      },
      {
         "uuid": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
         "area": "Гомельская",
         "country": "Республика Белаурсь",
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
   "uuid": "a89e15da-dc68-4179-9512-e2bdf04c18b2",
   "name": "Вася",
   "surname": "Дудкин",
   "sex": "MALE",
   "passport": {
      "series": "XB",
      "number": "333852"
   },
   "houseUUID": "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13",
   "createDate": "2024-01-25T18:05:00:839",
   "updateDate": "2024-01-25T18:05:00:839"
}
````

* **PUT update | Обновляет один Person в базе данных по uuid**
* http://localhost:8080/persons/863db796-cf16-4c67-ad24-710d0d2f0341
* Request example:
````json
{
   "name": "Женя",
   "surname": "Миля",
   "sex": "MALE",
   "passport": {
      "series": "BP",
      "number": "894379"
   },
   "houseUUID": "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15"
}
````
* Response example:
````json
{
   "uuid": "863db796-cf16-4c67-ad24-710d0d2f0341",
   "name": "Женя",
   "surname": "Миля",
   "sex": "MALE",
   "passport": {
      "series": "BP",
      "number": "894379"
   },
   "houseUUID": "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15",
   "createDate": "2023-05-11T11:00:00:000",
   "updateDate": "2024-01-25T18:05:54:997"
}
````

* **PATCH update | Обновляет данные одного Person в базе данных по uuid**
* http://localhost:8080/persons/f188d7be-146b-4668-a729-09a2d4fdc784
* Request example:
````json
{
   "name":"Егор",
   "sex":"MALE"
}
````
* Response example:
````json
{
   "uuid": "f188d7be-146b-4668-a729-09a2d4fdc784",
   "name": "Егор",
   "surname": "Калина",
   "sex": "MALE",
   "passport": {
      "series": "MN",
      "number": "789012"
   },
   "houseUUID": "d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14",
   "createDate": "2021-01-12T21:00:00:000",
   "updateDate": "2024-01-24T12:51:04:123"
}
````

* **DELETE delete | Удаляет один Person в базе данных по uuid**
* http://localhost:8080/persons/6cb850ef-298a-4e0b-9c46-bf3c527cd017