package ru.matvey.springrailwayfactory.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.matvey.springrailwayfactory.model.Cargo;
import ru.matvey.springrailwayfactory.repository.CargoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;


    public Cargo addCargo(String cargoName) {
        var cargo = Cargo.builder().name(cargoName).build();
        cargoRepository.save(cargo);
        return cargo;
    }

    public Cargo getCargoById(Long cargoId) {
        return cargoRepository.findById(cargoId).orElseThrow(EntityNotFoundException::new);
    }

    public List<Cargo> getAllCargo() {
        return cargoRepository.findAll();
    }

    public Cargo updateCargo(Long cargoId, String cargoName) {
        var cargo = getCargoById(cargoId);
        cargo.setName(cargoName);
        cargoRepository.save(cargo);
        return cargo;
    }

    //по идее нельзя удалять номенклатуру груза из справочника, ибо тогда будут проблемы с грузом в вагонах,
    //но по заданию должно быть реализовано, сделано в сервисе вагонов, чтобы избежать кольцевой зависимости hardDeleteCargo()
    public void deleteCargo(Long cargoId) {
        cargoRepository.deleteById(cargoId);
    }


}
