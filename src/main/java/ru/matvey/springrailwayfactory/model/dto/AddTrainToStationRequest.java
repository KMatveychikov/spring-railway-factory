package ru.matvey.springrailwayfactory.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddTrainToStationRequest {
    private Long stationId;
    private Long trainSheetId;
}
