package ru.clevertec.house.controller.api;

import by.clevertec.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Person", description = "API for working with persons")
public interface PersonOpenApi {

    @Operation(
            method = "GET",
            tags = "Person",
            description = "Get a person by uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of Person", example = "9aa78d35-fb66-45a6-8570-f81513ef8272")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NotFoundException.class),
                                    examples = @ExampleObject("""
                                            {
                                               "errorMessage": "Person with 9aa78d35-fb66-45a6-8570-f81513ef8277 not found!",
                                               "errorCode": "404 NOT_FOUND"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<PersonResponse> findById(@Parameter(example = "9aa78d35-fb66-45a6-8570-f81513ef8272") UUID uuid);

    @Operation(
            method = "GET",
            tags = "Person",
            description = "Get page of persons",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Page<PersonResponse>> findAll(@Parameter(hidden = true) Pageable pageable);

    @Operation(
            method = "GET",
            tags = "Person",
            description = "Get page of person which live in a house with uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of house", example = "d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Page<PersonResponse>> findPersonsWhichLiveInHouse(@Parameter(example = "d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14") UUID uuid,
                                                                     @Parameter(hidden = true) Pageable pageable);

    @Operation(
            method = "GET",
            tags = "Person",
            description = "Get page of person which have ever lived in the house with uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of house", example = "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Page<PersonResponse>> findPersonsWhichSomeTimeLiveInHouse(@Parameter(example = "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15") UUID uuid,
                                                                             @Parameter(hidden = true) Pageable pageable);

    @Operation(
            method = "GET",
            tags = "Person",
            description = "Get page of person which have ever owned the house with uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of house", example = "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Page<PersonResponse>> findPersonsWhichSomeTimeOwnHouse(@Parameter(example = "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15") UUID uuid,
                                                                          @Parameter(hidden = true) Pageable pageable);

    @Operation(
            method = "GET",
            tags = "Person",
            description = "Get page of persons found by the search element",
            parameters = {
                    @Parameter(name = "searchterm", description = "the search element", example = "ар")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Page<PersonResponse>> findPersonsFullTextSearch(@Parameter(example = "ар") String searchterm,
                                                                   @Parameter(hidden = true) Pageable pageable);

    @Operation(
            method = "POST",
            tags = "Person",
            description = "Create a new person",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PersonRequest.class),
                            examples = @ExampleObject("""
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
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<PersonResponse> save(PersonRequest personRequest);

    @Operation(
            method = "PUT",
            tags = "Person",
            description = "Update a person with uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of person", example = "863db796-cf16-4c67-ad24-710d0d2f0341")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PersonRequest.class),
                            examples = @ExampleObject("""
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
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<PersonResponse> update(@Parameter(example = "863db796-cf16-4c67-ad24-710d0d2f0341") UUID uuid,
                                          PersonRequest personRequest);

    @Operation(
            method = "PATCH",
            tags = "Person",
            description = "PatchUpdate a person with uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of person", example = "f188d7be-146b-4668-a729-09a2d4fdc784")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject("""
                                    {
                                       "name":"Егор",
                                       "sex":"MALE"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<PersonResponse> patchUpdate(@Parameter(example = "f188d7be-146b-4668-a729-09a2d4fdc784") UUID uuid,
                                               Map<String, Object> fields);

    @Operation(
            method = "DELETE",
            tags = "Person",
            description = "Delete a person by uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of person", example = "6cb850ef-298a-4e0b-9c46-bf3c527cd017")
            },
            responses = {
                    @ApiResponse(responseCode = "200")
            }
    )
    ResponseEntity<Void> delete(@Parameter(example = "6cb850ef-298a-4e0b-9c46-bf3c527cd017") UUID uuid);

}
