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
import ru.clevertec.house.dto.request.HouseRequest;
import ru.clevertec.house.dto.response.HouseResponse;
import ru.clevertec.house.service.HouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
public class HouseController {

    private final HouseService houseService;

    @GetMapping("/{uuid}")
    public ResponseEntity<HouseResponse> findById(@PathVariable UUID uuid) {

        return ResponseEntity.ok(houseService.findById(uuid));
    }

    @GetMapping
    public ResponseEntity<Page<HouseResponse>> findAll(@PageableDefault(15) Pageable pageable) {

        Page<HouseResponse> housesPage = houseService.findAll(pageable);

        return ResponseEntity.ok(housesPage);
    }

    @GetMapping("/sometimelives/{uuid}")
    public ResponseEntity<Page<HouseResponse>> findHousesWhichSomeTimeLivesPerson(@PathVariable UUID uuid,
                                                                                  @PageableDefault(15) Pageable pageable) {
        Page<HouseResponse> housesPage = houseService.findHousesWhichSomeTimeLivesPerson(uuid, pageable);

        return ResponseEntity.ok(housesPage);
    }

    @GetMapping("/owns/{uuid}")
    public ResponseEntity<Page<HouseResponse>> findHousesWhichOwnPerson(@PathVariable UUID uuid,
                                                                        @PageableDefault(15) Pageable pageable) {
        Page<HouseResponse> housesPage = houseService.findHousesWhichOwnPerson(uuid, pageable);

        return ResponseEntity.ok(housesPage);
    }

    @GetMapping("/sometimeowns/{uuid}")
    public ResponseEntity<Page<HouseResponse>> findHousesWhichSomeTimeOwnPerson(@PathVariable UUID uuid,
                                                                                @PageableDefault(15) Pageable pageable) {
        Page<HouseResponse> housesPage = houseService.findHousesWhichSomeTimeOwnPerson(uuid, pageable);

        return ResponseEntity.ok(housesPage);
    }

    @GetMapping("/fullsearch/{searchterm}")
    public ResponseEntity<Page<HouseResponse>> findHousesFullTextSearch(@PathVariable String searchterm,
                                                                        @PageableDefault(15) Pageable pageable) {
        Page<HouseResponse> housesPage = houseService.findHousesFullTextSearch(searchterm, pageable);

        return ResponseEntity.ok(housesPage);
    }

    @PostMapping
    public ResponseEntity<HouseResponse> save(@RequestBody HouseRequest houseRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(houseService.save(houseRequest));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<HouseResponse> update(@PathVariable UUID uuid,
                                                @RequestBody HouseRequest houseRequest) {

        return ResponseEntity.ok(houseService.update(uuid, houseRequest));
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<HouseResponse> patchUpdate(@PathVariable UUID uuid, @RequestBody Map<String, Object> fields) {

        return ResponseEntity.ok(houseService.patchUpdate(uuid, fields));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {

        houseService.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}