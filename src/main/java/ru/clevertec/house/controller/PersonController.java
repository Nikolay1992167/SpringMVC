package ru.clevertec.house.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.house.controller.api.PersonOpenApi;
import ru.clevertec.house.dto.request.PersonRequest;
import ru.clevertec.house.dto.response.PersonResponse;
import ru.clevertec.house.service.PersonService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController implements PersonOpenApi {

    private final PersonService personService;

    @GetMapping("/{uuid}")
    public ResponseEntity<PersonResponse> findById(@PathVariable UUID uuid) {

        return ResponseEntity.ok(personService.findById(uuid));
    }

    @GetMapping
    public ResponseEntity<Page<PersonResponse>> findAll(@PageableDefault(15) Pageable pageable) {

        Page<PersonResponse> personsPage = personService.findAll(pageable);

        return ResponseEntity.ok(personsPage);
    }

    @GetMapping("/lives/{uuid}")
    public ResponseEntity<Page<PersonResponse>> findPersonsWhichLiveInHouse(@PathVariable UUID uuid,
                                                                            @PageableDefault(15) Pageable pageable) {
        Page<PersonResponse> personPage = personService.findPersonsWhichLiveInHouse(uuid, pageable);

        return ResponseEntity.ok(personPage);
    }

    @GetMapping("/sometimelives/{uuid}")
    public ResponseEntity<Page<PersonResponse>> findPersonsWhichSomeTimeLiveInHouse(@PathVariable UUID uuid,
                                                                                    @PageableDefault(15) Pageable pageable) {
        Page<PersonResponse> personsPage = personService.findPersonsWhichSomeTimeLiveInHouse(uuid, pageable);

        return ResponseEntity.ok(personsPage);
    }

    @GetMapping("/sometimeownes/{uuid}")
    public ResponseEntity<Page<PersonResponse>> findPersonsWhichSomeTimeOwnHouse(@PathVariable UUID uuid,
                                                                                 @PageableDefault(15) Pageable pageable) {
        Page<PersonResponse> personsPage = personService.findPersonsWhichSomeTimeOwnHouse(uuid, pageable);

        return ResponseEntity.ok(personsPage);
    }

    @GetMapping("/fullsearch/{searchterm}")
    public ResponseEntity<Page<PersonResponse>> findPersonsFullTextSearch(@PathVariable String searchterm,
                                                                          @PageableDefault(15) Pageable pageable) {
        Page<PersonResponse> personsPage = personService.findPersonsFullTextSearch(searchterm, pageable);

        return ResponseEntity.ok(personsPage);
    }

    @PostMapping
    public ResponseEntity<PersonResponse> save(@RequestBody PersonRequest personRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personService.save(personRequest));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<PersonResponse> update(@PathVariable UUID uuid,
                                                 @RequestBody PersonRequest personRequest) {

        return ResponseEntity.ok(personService.update(uuid, personRequest));
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<PersonResponse> patchUpdate(@PathVariable UUID uuid, @RequestBody Map<String, Object> fields) {

        return ResponseEntity.ok(personService.patchUpdate(uuid, fields));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {

        personService.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}