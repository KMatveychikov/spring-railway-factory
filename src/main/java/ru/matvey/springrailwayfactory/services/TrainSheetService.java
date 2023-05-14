package ru.matvey.springrailwayfactory.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.matvey.springrailwayfactory.enums.TrainSheetStatus;
import ru.matvey.springrailwayfactory.enums.WagonStatus;
import ru.matvey.springrailwayfactory.model.TrainSheet;
import ru.matvey.springrailwayfactory.model.Wagon;
import ru.matvey.springrailwayfactory.repository.TrainSheetRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainSheetService {
    private final TrainSheetRepository trainSheetRepository;
    private final WagonService wagonService;

    public TrainSheet addTrainSheet() {
        TrainSheet trainSheet = TrainSheet.builder()
                .created(LocalDateTime.now())
                .status(TrainSheetStatus.Created)
                .wagons(new ArrayList<>())
                .build();
        trainSheetRepository.save(trainSheet);
        log.info("TrainSheet {} created", trainSheet.getId());
        return trainSheet;
    }

    public TrainSheet addWagonToTrainSheet(Long trainSheetId, Long wagonId) throws Exception {
        TrainSheet trainSheet = getTrainSheetById(trainSheetId);
        Wagon wagon = wagonService.getWagonById(wagonId);
        List<Wagon> trainSheetWagons = trainSheet.getWagons();
        if (wagon.getStatus() != WagonStatus.OnStation && wagon.getStatus() != WagonStatus.Unsorted) {
            trainSheetWagons.add(wagon);
            trainSheet.setWagons(trainSheetWagons);
            wagonService.setWagonOrderNumber(wagonId,trainSheet.getWagons().size()+1);
            trainSheetRepository.save(trainSheet);
            log.info("Wagon {} added to train sheet {}", wagonId, trainSheetId);
        } else {
            log.warn("this wagon {} cannot be added to train sheet", wagonId);
            throw new Exception("Этот вагон нельзя добавть к натурному листу");
        }

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

    public TrainSheet updateTrainSheet(Long id, List<Wagon> wagons, TrainSheetStatus status) {
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
