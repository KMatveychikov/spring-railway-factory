package ru.matvey.springrailwayfactory.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.matvey.springrailwayfactory.model.Cargo;
import ru.matvey.springrailwayfactory.repository.CargoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CargoService {

    private final CargoRepository cargoRepository;


    public Cargo addCargo(String cargoName) {
        Cargo cargo = Cargo.builder()
                .name(cargoName)
                .build();
        cargoRepository.save(cargo);
        log.info("Cargo {} created", cargo);
        return cargo;
    }

    public Cargo getCargoById(Long cargoId) {
        return cargoRepository.findById(cargoId).orElseThrow(EntityNotFoundException::new);
    }

    public List<Cargo> getAllCargo() {
        return cargoRepository.findAll();
    }

    public Cargo updateCargo(Long cargoId, String cargoName) {
        Cargo cargo = getCargoById(cargoId);
        cargo.setName(cargoName);
        cargoRepository.save(cargo);
        log.info("Cargo {} updated", cargo);
        return cargo;
    }

    //по идее нельзя удалять номенклатуру груза из справочника, ибо тогда будут проблемы с грузом в вагонах,
    //но по заданию должно быть реализовано, сделано в сервисе вагонов, чтобы избежать кольцевой зависимости hardDeleteCargo()
    public void deleteCargo(Long cargoId) {
        log.info("Cargo {} deleted", getCargoById(cargoId));
        cargoRepository.deleteById(cargoId);
    }


}
