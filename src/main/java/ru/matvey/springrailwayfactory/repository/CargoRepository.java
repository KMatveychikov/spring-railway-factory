package ru.matvey.springrailwayfactory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.matvey.springrailwayfactory.model.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
}
