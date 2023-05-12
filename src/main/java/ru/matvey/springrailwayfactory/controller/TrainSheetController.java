package ru.matvey.springrailwayfactory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matvey.springrailwayfactory.model.TrainSheet;
import ru.matvey.springrailwayfactory.model.dto.UpdateTrainSheetRequest;
import ru.matvey.springrailwayfactory.services.TrainSheetService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/train")
@RequiredArgsConstructor
public class TrainSheetController {

    private final TrainSheetService trainSheetService;

    @PostMapping("/add")
    public ResponseEntity<TrainSheet> addTrainSheet() {
        return ResponseEntity.ok(trainSheetService.addTrainSheet());
    }

    @GetMapping("/all")
    public ResponseEntity<List<TrainSheet>> getAllTrainSheet() {
        return ResponseEntity.ok(trainSheetService.getAllTrainSheets());
    }

    @GetMapping("/get")
    public ResponseEntity<TrainSheet> getTrainSheetById(@RequestParam Long id) {
        return ResponseEntity.ok(trainSheetService.getTrainSheetById(id));
    }

    @PutMapping("/update")
    public ResponseEntity<TrainSheet> updateTrainSheet(@RequestBody UpdateTrainSheetRequest request) {
        return ResponseEntity.ok(trainSheetService.updateTrainSheet(request.getTrainSheetId(), request.getWagons(), request.getStatus()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTrainSheet(@RequestParam Long id) {
        trainSheetService.deleteTrainSheet(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
