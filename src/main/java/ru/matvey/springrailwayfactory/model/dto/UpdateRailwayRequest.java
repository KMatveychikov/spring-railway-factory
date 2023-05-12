package ru.matvey.springrailwayfactory.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.matvey.springrailwayfactory.model.Wagon;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRailwayRequest {
    private Long railwayId;
    private List<Wagon> wagons;
}
