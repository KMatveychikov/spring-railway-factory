package ru.matvey.springrailwayfactory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.matvey.springrailwayfactory.model.Railway;

public interface RailwayRepository extends JpaRepository<Railway, Long> {
}
