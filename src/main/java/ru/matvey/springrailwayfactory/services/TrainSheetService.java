package ru.matvey.springrailwayfactory.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.matvey.springrailwayfactory.enums.TrainSheetStatus;
import ru.matvey.springrailwayfactory.model.TrainSheet;
import ru.matvey.springrailwayfactory.model.Wagon;
import ru.matvey.springrailwayfactory.repository.TrainSheetRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainSheetService {
    private final TrainSheetRepository trainSheetRepository;

    public TrainSheet addTrainSheet() {
        var trainSheet = TrainSheet.builder()
                .created(LocalDateTime.now())
                .status(TrainSheetStatus.Created)
                .wagons(new ArrayList<>())
                .build();
        trainSheetRepository.save(trainSheet);
        return trainSheet;
    }

    public TrainSheet getTrainSheetById(Long id) {
        return trainSheetRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<TrainSheet> getAllTrainSheets() {
        return trainSheetRepository.findAll();
    }

    public TrainSheet setTrainSheetStatus(Long id, TrainSheetStatus status) {
        TrainSheet trainSheet = getTrainSheetById(id);
        trainSheet.setStatus(status);
        trainSheet.setUpdated(LocalDateTime.now());
        trainSheetRepository.save(trainSheet);
        return trainSheet;
    }

    public TrainSheet updateTrainSheet(Long id, List<Wagon> wagons, TrainSheetStatus status){
        var trainSheet = getTrainSheetById(id);
        trainSheet.setWagons(wagons);
        trainSheet.setStatus(status);
        trainSheet.setUpdated(LocalDateTime.now());
        trainSheetRepository.save(trainSheet);
        return trainSheet;
    }

    public ResponseEntity<?> deleteTrainSheet(Long id) {
        trainSheetRepository.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
