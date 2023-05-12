package ru.matvey.springrailwayfactory.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.matvey.springrailwayfactory.model.Cargo;
import ru.matvey.springrailwayfactory.model.dto.UpdateWagonRequest;
import ru.matvey.springrailwayfactory.enums.WagonStatus;
import ru.matvey.springrailwayfactory.model.dto.AddWagonRequest;
import ru.matvey.springrailwayfactory.model.Wagon;
import ru.matvey.springrailwayfactory.model.WagonPassport;
import ru.matvey.springrailwayfactory.repository.WagonRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WagonService {
    private final WagonRepository wagonRepository;
    private final CargoService cargoService;

    public Wagon addWagon(AddWagonRequest request) {
        var wagon = Wagon.builder()
                .type(request.getType())
                .wagonWeight(request.getWagonWeight())
                .wagonCapacity(request.getWagonCapacity())
                .orderNumber(request.getOrderNumber())
                .cargo(cargoService.getCargoById(request.getCargoId()))
                .cargoWeight(request.getCargoWeight())
                .status(WagonStatus.Created)
                .build();

        wagonRepository.save(wagon);
        return wagon;

    }

    public Wagon getWagonById(Long wagonId) {
        return wagonRepository.findById(wagonId).orElseThrow(EntityNotFoundException::new);
    }

    public List<Wagon> getAllWagons() {
        return wagonRepository.findAll();
    }

    public void setWagonStatus(Long wagonId, WagonStatus status) {
        var wagon = getWagonById(wagonId);
        wagon.setStatus(status);
        wagonRepository.save(wagon);
    }

    public void setWagonStation(Long wagonId, Long stationId) {
        var wagon = getWagonById(wagonId);
        wagon.setStationId(stationId);
        wagonRepository.save(wagon);
    }

    public void setWagonRailway(Long wagonId, Long railwayId) {
        var wagon = getWagonById(wagonId);
        wagon.setRailwayId(railwayId);
        wagonRepository.save(wagon);
    }

    public void setWagonLastMove(Long wagonId) {
        var wagon = getWagonById(wagonId);
        wagon.setLastMove(LocalDateTime.now());
        wagonRepository.save(wagon);

    }
    public void setWagonCargo(Long wagonId, Cargo cargo) {
        var wagon = getWagonById(wagonId);
        wagon.setCargo(cargo);
        wagonRepository.save(wagon);
    }

    public Wagon updateWagon(UpdateWagonRequest request) {
        var wagon = getWagonById(request.getWagonId());
        wagon.setType(request.getType());
        wagon.setWagonWeight(request.getWagonWeight());
        wagon.setWagonCapacity(request.getWagonCapacity());
        wagon.setOrderNumber(request.getOrderNumber());
        wagon.setCargo(cargoService.getCargoById(request.getCargoId()));
        wagon.setCargoWeight(request.getCargoWeight());
        wagon.setStatus(request.getStatus());
        if (!Objects.equals(wagon.getStationId(), request.getStationId())) wagon.setLastMove(LocalDateTime.now());
        if (!Objects.equals(wagon.getRailwayId(), request.getRailwayId())) wagon.setLastMove(LocalDateTime.now());
        wagon.setStationId(request.getStationId());
        wagon.setRailwayId(request.getRailwayId());

        wagonRepository.save(wagon);
        return wagon;
    }

    public void deleteWagon(Long wagonId) throws Exception {
        var wagon = getWagonById(wagonId);
        if (wagon.getStatus() == WagonStatus.Created || wagon.getStatus() == WagonStatus.Leaved) {
            wagonRepository.deleteById(wagonId);
        } else {
            throw new Exception("Невозможно удалить вагон прямо с путей на станции");
        }
    }

    public WagonPassport getWagonPassport(Long wagonId) {
        var wagon = getWagonById(wagonId);
        return WagonPassport.builder()
                .id(wagon.getWagonNumber())
                .type(wagon.getType())
                .wagonWeight(wagon.getWagonWeight())
                .wagonCapacity(wagon.getWagonCapacity())
                .build();
    }
    //метод для таможенников, росчеком на документах удаляем груз из номенклатуры, и из всех вагонов
    public void hardDeleteCargo(Long cargoId) {
        List<Wagon> wagons = getAllWagons();
        wagons.stream().peek(wagon -> {
            if(Objects.equals(wagon.getCargo().getId(), cargoId)) setWagonCargo(wagon.getWagonNumber(), null);
        }).toList();
        cargoService.deleteCargo(cargoId);
    }
}
