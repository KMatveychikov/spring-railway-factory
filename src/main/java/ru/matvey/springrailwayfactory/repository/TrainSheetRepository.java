package ru.matvey.springrailwayfactory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.matvey.springrailwayfactory.model.TrainSheet;

public interface TrainSheetRepository extends JpaRepository<TrainSheet, Long> {
}
