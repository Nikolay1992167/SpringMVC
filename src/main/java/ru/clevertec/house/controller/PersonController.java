package ru.clevertec.house.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PaginationResponse;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.service.JdbcService;
import ru.clevertec.house.service.PersonService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    private final JdbcService jdbcService;

    @GetMapping("/{uuid}")
    public ResponseEntity<PersonResponse> findById(@PathVariable UUID uuid) {

        return ResponseEntity.ok(personService.findById(uuid));
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<PersonResponse>> findAll(@RequestParam(defaultValue = "1") int pageNumber,
                                                                      @RequestParam(defaultValue = "15") int pageSize) {

        List<PersonResponse> persons = personService.findAll(pageNumber, pageSize);

        PaginationResponse<PersonResponse> response = new PaginationResponse<>();
        response.setCurrentPage(pageNumber);
        response.setTotalPages(persons.size()/pageSize);
        response.setTotalItems(persons.size());
        response.setData(persons);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/lives/{uuid}")
    public ResponseEntity<List<PersonResponse>> findPersonWhichLiveInHouse(@PathVariable UUID uuid) {

        return ResponseEntity.ok(jdbcService.findPersonWhichLiveInHouse(uuid));
    }

    @GetMapping("/fullsearch/{searchterm}")
    public ResponseEntity<List<PersonResponse>> findPersonsFullTextSearch(@PathVariable String searchterm) {

        return ResponseEntity.ok(jdbcService.findPersonsFullTextSearch(searchterm));
    }

    @PostMapping
    public ResponseEntity<PersonResponse> save(@RequestBody PersonRequest personRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personService.save(personRequest));
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<PersonResponse> patchUpdate(@PathVariable UUID uuid, @RequestBody Map<String, Object> fields) {

        return ResponseEntity.ok(personService.patchUpdate(uuid, fields));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<PersonResponse> update(@PathVariable UUID uuid,
                                                 @RequestBody PersonRequest personRequest) {

        return ResponseEntity.ok(personService.update(uuid, personRequest));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {

        personService.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}