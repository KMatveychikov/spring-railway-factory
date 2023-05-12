package ru.matvey.springrailwayfactory.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.matvey.springrailwayfactory.enums.WagonStatus;
import ru.matvey.springrailwayfactory.enums.WagonType;

@Data
@RequiredArgsConstructor
public class UpdateWagonRequest {
    private Long wagonId;
    private WagonType type;
    private float wagonWeight;
    private float wagonCapacity;
    private int orderNumber;
    private Long cargoId;
    private float cargoWeight;
    private WagonStatus status;
    private Long stationId;
    private Long railwayId;
}
