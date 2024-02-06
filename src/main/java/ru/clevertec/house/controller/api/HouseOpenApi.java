package ru.clevertec.house.controller.api;

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
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.exception.HouseNotEmptyException;
import ru.clevertec.house.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

@Tag(name = "House", description = "API for working with houses")
public interface HouseOpenApi {

    @Operation(
            method = "GET",
            tags = "House",
            description = "Get a house by uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of House", example = "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HouseResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                "uuid": "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6",
                                                "area": "Гомельская",
                                                "country": "Беларусь",
                                                "city": "Ельск",
                                                "street": "Ленина",
                                                "number": 2,
                                               "createDate":"2023-12-30T12:00:00:000"
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
                                                "errorMessage": "House with 0699cfd2-9fb7-4483-bcdf-194a2c6b7fe9 not found!",
                                                "errorCode": "404 NOT_FOUND"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<HouseResponse> findById(@Parameter(example = "0699cfd2-9fb7-4483-bcdf-194a2c6b7fe6") UUID uuid);

    @Operation(
            method = "GET",
            tags = "House",
            description = "Get page of houses",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HouseResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Page<HouseResponse>> findAll(@Parameter(hidden = true) Pageable pageable);

    @Operation(
            method = "GET",
            tags = "House",
            description = "Get page of houses where a person with uuid has ever lived",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of Person", example = "922e0213-e543-48ef-b8cb-92592afd5100")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HouseResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Page<HouseResponse>> findHousesWhichSomeTimeLivesPerson(@Parameter(example = "922e0213-e543-48ef-b8cb-92592afd5100") UUID uuid,
                                                                           @Parameter(hidden = true) Pageable pageable);

    @Operation(
            method = "GET",
            tags = "House",
            description = "Get page of houses owned by a person with uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of Person", example = "63a1faca-a963-4d4b-bfb9-2dafaedc36fe")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HouseResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Page<HouseResponse>> findHousesWhichOwnPerson(@Parameter(example = "63a1faca-a963-4d4b-bfb9-2dafaedc36fe") UUID uuid,
                                                                 @Parameter(hidden = true) Pageable pageable);

    @Operation(
            method = "GET",
            tags = "House",
            description = "Get page of houses that a person has ever owned with uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of Person", example = "863db796-cf16-4c67-ad24-710d0d2f0341")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HouseResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Page<HouseResponse>> findHousesWhichSomeTimeOwnPerson(@Parameter(example = "863db796-cf16-4c67-ad24-710d0d2f0341") UUID uuid,
                                                                         @Parameter(hidden = true) Pageable pageable);

    @Operation(
            method = "GET",
            tags = "House",
            description = "Get page of houses found by the search element",
            parameters = {
                    @Parameter(name = "searchterm", description = "the search element", example = "ви")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HouseResponse.class),
                                    examples = @ExampleObject("""
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
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Page<HouseResponse>> findHousesFullTextSearch(@Parameter(example = "ви") String searchterm,
                                                                 @Parameter(hidden = true) Pageable pageable);

    @Operation(
            method = "POST",
            tags = "House",
            description = "Create a new house",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HouseRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "area": "Могилевская",
                                      "country": "Беларусь",
                                      "city": "Бобруйск",
                                      "street": "Минская",
                                      "number": 19
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HouseResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                              "uuid": "3193f8d7-7501-4771-888b-6963977b4b55",
                                              "area": "Могилевская",
                                              "country": "Беларусь",
                                              "city": "Бобруйск",
                                              "street": "Минская",
                                              "number": 19,
                                              "createDate": "2024-01-25T17:50:45:565"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<HouseResponse> save(HouseRequest houseRequest);

    @Operation(
            method = "PUT",
            tags = "House",
            description = "Update a house with uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of house", example = "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HouseRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "area": "Витебская",
                                      "country": "Беларусь",
                                      "city": "Орша",
                                      "street": "Минская",
                                      "number": 10
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HouseResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                              "uuid": "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15",
                                              "area": "Витебская",
                                              "country": "Беларусь",
                                              "city": "Орша",
                                              "street": "Минская",
                                              "number": 10,
                                              "createDate": "2003-03-18T12:00:00:000"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<HouseResponse> update(@Parameter(example = "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15") UUID uuid,
                                         HouseRequest houseRequest);

    @Operation(
            method = "PATCH",
            tags = "House",
            description = "PatchUpdate a house with uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of house", example = "9724b9b8-216d-4ab9-92eb-e6e06029580d")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject("""
                                    {
                                       "street": "Северная",
                                       "number": 126
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HouseResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                               "uuid": "9724b9b8-216d-4ab9-92eb-e6e06029580d",
                                               "area": "Гомельская",
                                               "country": "Беларусь",
                                               "city": "Мозырь",
                                               "street": "Северная",
                                               "number": 126,
                                               "createDate": "1998-08-12T12:00:00:000"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<HouseResponse> patchUpdate(@Parameter(example = "9724b9b8-216d-4ab9-92eb-e6e06029580d") UUID uuid,
                                              Map<String, Object> fields);

    @Operation(
            method = "DELETE",
            tags = "House",
            description = "Delete a house by uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Uuid of house", example = "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15")
            },
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(
                            responseCode = "409",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HouseNotEmptyException.class),
                                    examples = @ExampleObject("""
                                            {
                                                "errorMessage": "Cannot delete house: there are residents living in it",
                                                "errorCode": "409 CONFLICT"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Void> delete(@Parameter(example = "e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15") UUID uuid);
}
