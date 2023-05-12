package ru.matvey.springrailwayfactory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matvey.springrailwayfactory.model.Station;
import ru.matvey.springrailwayfactory.model.dto.UpdateStationRequest;
import ru.matvey.springrailwayfactory.services.StationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/station")
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;


    @PostMapping("/add")
    public ResponseEntity<Station> addStation() {
        return ResponseEntity.ok(stationService.addStation());
    }
    @GetMapping("/all")
    public ResponseEntity<List<Station>> getAllStation(){
        return ResponseEntity.ok(stationService.getAllStations());
    }
    @GetMapping("/get")
    public ResponseEntity<Station> getStationById(@RequestParam Long id){
        return ResponseEntity.ok(stationService.getStationById(id));
    }

    @PutMapping("/update")
    public ResponseEntity<Station> updateStation(@RequestBody UpdateStationRequest request){
        return ResponseEntity.ok(stationService.updateStation(request.getStationId(), request.getRailwayList()));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteStation(@RequestParam Long id) throws Exception {
        stationService.deleteStation(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
