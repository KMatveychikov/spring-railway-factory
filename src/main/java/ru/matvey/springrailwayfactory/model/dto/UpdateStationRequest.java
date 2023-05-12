package ru.matvey.springrailwayfactory.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.matvey.springrailwayfactory.model.Railway;

import java.util.List;
@Data
@AllArgsConstructor
public class UpdateStationRequest {
    private Long stationId;
    private List<Railway> railwayList;
}
