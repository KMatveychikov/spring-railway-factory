package ru.matvey.springrailwayfactory.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.matvey.springrailwayfactory.enums.TrainSheetStatus;
import ru.matvey.springrailwayfactory.model.Wagon;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public class UpdateTrainSheetRequest {
    private Long trainSheetId;
    private List<Wagon> wagons;
    private TrainSheetStatus status;
}
