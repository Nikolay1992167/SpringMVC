package by.clevertec.controller;

import by.clevertec.dto.request.HouseRequest;
import by.clevertec.dto.response.HouseResponse;
import by.clevertec.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    public ResponseEntity<List<HouseResponse>> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                                                       @RequestParam(defaultValue = "15") int pageSize) {
        return ResponseEntity.ok(houseService.findAll(pageNumber, pageSize));
    }

    @PostMapping
    public ResponseEntity<HouseResponse> save(@RequestBody HouseRequest houseRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(houseService.save(houseRequest));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<HouseResponse> update(@PathVariable UUID uuid,
                                                @RequestBody HouseRequest houseRequest){
        return ResponseEntity.ok(houseService.update(uuid, houseRequest));
    }

    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<Void> deleteGiftCertificate(@PathVariable UUID uuid) {
        houseService.delete(uuid);
        return ResponseEntity.status(204).build();
    }
}
