package ru.matvey.springrailwayfactory.model;

import jakarta.persistence.*;
import lombok.*;
import ru.matvey.springrailwayfactory.enums.WagonStatus;
import ru.matvey.springrailwayfactory.enums.WagonType;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Wagon {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long wagonNumber;
    @Enumerated
    private WagonType type;
    private float wagonWeight;
    private float wagonCapacity;
    private int orderNumber;
    @Enumerated
    private WagonStatus status;
    @ManyToOne
    private Cargo cargo;
    private float cargoWeight;

    private Long stationId;
    private Long railwayId;
    private LocalDateTime lastMove;
}
