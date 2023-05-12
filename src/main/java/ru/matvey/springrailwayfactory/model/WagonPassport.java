package ru.matvey.springrailwayfactory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.matvey.springrailwayfactory.enums.WagonType;

@Data
@Builder
@AllArgsConstructor
public class WagonPassport {
    private Long id;
    private WagonType type;
    private float wagonWeight;
    private float wagonCapacity;
}
