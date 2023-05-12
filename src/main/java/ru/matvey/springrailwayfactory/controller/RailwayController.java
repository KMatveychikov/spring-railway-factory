package ru.matvey.springrailwayfactory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matvey.springrailwayfactory.model.Railway;
import ru.matvey.springrailwayfactory.model.dto.UpdateRailwayRequest;
import ru.matvey.springrailwayfactory.services.RailwayService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/railway")
@RequiredArgsConstructor
public class RailwayController {

    private final RailwayService railwayService;

    @PostMapping("/add")
    public ResponseEntity<Railway> addRailway() {
        return ResponseEntity.ok(railwayService.addRailway());
    }
    @GetMapping("/all")
    public ResponseEntity<List<Railway>> getAllRailway(){
        return ResponseEntity.ok(railwayService.getAllRailways());
    }
    @GetMapping("/get")
    public ResponseEntity<Railway> getRailwayById(@RequestParam Long id){
        return ResponseEntity.ok(railwayService.getRailwayById(id));
    }

    @PutMapping("/update")
    public ResponseEntity<Railway> updateRailway(@RequestBody UpdateRailwayRequest request){
        return ResponseEntity.ok(railwayService.updateRailway(request.getRailwayId(), request.getWagons()));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRailway(@RequestParam Long id) throws Exception {
        railwayService.deleteRailway(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
