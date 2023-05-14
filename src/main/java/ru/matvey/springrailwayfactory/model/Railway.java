package ru.matvey.springrailwayfactory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.matvey.springrailwayfactory.enums.RailwayStatus;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Railway {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @OneToMany
    private List<Wagon> wagons;
    @Enumerated
    private RailwayStatus status;
}
