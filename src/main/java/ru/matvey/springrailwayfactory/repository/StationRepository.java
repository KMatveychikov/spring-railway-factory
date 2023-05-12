package ru.matvey.springrailwayfactory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.matvey.springrailwayfactory.model.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}
