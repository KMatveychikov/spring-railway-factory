package ru.matvey.springrailwayfactory.model;

import jakarta.persistence.*;
import lombok.*;
import ru.matvey.springrailwayfactory.enums.TrainSheetStatus;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TrainSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private TrainSheetStatus status;
    @OneToMany
    List<Wagon> wagons;

}
