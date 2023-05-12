package ru.matvey.springrailwayfactory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matvey.springrailwayfactory.model.Wagon;
import ru.matvey.springrailwayfactory.model.dto.AddWagonRequest;
import ru.matvey.springrailwayfactory.model.dto.UpdateWagonRequest;
import ru.matvey.springrailwayfactory.services.WagonService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wagon")
@RequiredArgsConstructor
public class WagonController {

    private final WagonService wagonService;

    @PostMapping("/add")
    public ResponseEntity<Wagon> addWagon(@RequestBody AddWagonRequest request) {
        return ResponseEntity.ok(wagonService.addWagon(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Wagon>> getAllWagons() {
        return ResponseEntity.ok(wagonService.getAllWagons());
    }

    @GetMapping("/get")
    public ResponseEntity<Wagon> getWagonById(@RequestParam Long id) {
        return ResponseEntity.ok(wagonService.getWagonById(id));
    }

    @PutMapping("/update")
    public ResponseEntity<Wagon> updateWagon(@RequestBody UpdateWagonRequest request) {
        return ResponseEntity.ok(wagonService.updateWagon(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteWagon(@RequestParam Long id) throws Exception {
        wagonService.deleteWagon(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
