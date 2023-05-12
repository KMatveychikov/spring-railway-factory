package ru.matvey.springrailwayfactory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @OneToMany
    private List<Railway> railways;
    @OneToMany
    private List<Wagon> unsortedWagons;
}
