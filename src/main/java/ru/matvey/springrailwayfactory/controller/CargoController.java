package ru.matvey.springrailwayfactory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matvey.springrailwayfactory.model.Cargo;
import ru.matvey.springrailwayfactory.model.dto.AddCargoRequest;
import ru.matvey.springrailwayfactory.model.dto.UpdateCargoRequest;
import ru.matvey.springrailwayfactory.services.CargoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cargo")
@RequiredArgsConstructor
public class CargoController {
    private final CargoService cargoService;

    @PostMapping("/add")
    public ResponseEntity<Cargo> addCargo(@RequestBody AddCargoRequest request) {
        return ResponseEntity.ok(cargoService.addCargo(request.getName()));
    }
    @GetMapping("/all")
    public ResponseEntity<List<Cargo>> getAllCargo(){
        return ResponseEntity.ok(cargoService.getAllCargo());
    }
    @GetMapping("/get")
    public ResponseEntity<Cargo> getCargoById(@RequestParam Long id){
        return ResponseEntity.ok(cargoService.getCargoById(id));
    }

    @PutMapping("/update")
    public ResponseEntity<Cargo> updateCargo(@RequestBody UpdateCargoRequest request){
        return ResponseEntity.ok(cargoService.updateCargo(request.getCargoId(), request.getCargoName()));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCargo(@RequestParam Long id){
        cargoService.deleteCargo(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
