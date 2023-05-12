package ru.matvey.springrailwayfactory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.matvey.springrailwayfactory.model.Wagon;

public interface WagonRepository extends JpaRepository<Wagon, Long> {
}
