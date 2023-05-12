package ru.matvey.springrailwayfactory.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.matvey.springrailwayfactory.enums.WagonType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddWagonRequest {

    private WagonType type;
    private float wagonWeight;
    private float wagonCapacity;
    private int orderNumber;
    private Long cargoId;
    private float cargoWeight;
}
