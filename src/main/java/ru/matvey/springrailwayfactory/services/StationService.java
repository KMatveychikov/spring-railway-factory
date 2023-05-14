package ru.matvey.springrailwayfactory.services;

import com.google.common.collect.Iterables;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.matvey.springrailwayfactory.enums.RailwayStatus;
import ru.matvey.springrailwayfactory.enums.TrainSheetStatus;
import ru.matvey.springrailwayfactory.enums.WagonStatus;
import ru.matvey.springrailwayfactory.model.Railway;
import ru.matvey.springrailwayfactory.model.Station;
import ru.matvey.springrailwayfactory.model.TrainSheet;
import ru.matvey.springrailwayfactory.model.Wagon;
import ru.matvey.springrailwayfactory.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationService {
    private final StationRepository stationRepository;
    private final TrainSheetService trainSheetService;
    private final RailwayService railwayService;
    private final WagonService wagonService;

    public Station addStation() {
        Station station = Station.builder().railways(new ArrayList<>()).build();
        stationRepository.save(station);
        log.info("Station {} created", station.getId());
        return station;
    }

    public Station getStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(EntityNotFoundException::new);
    }

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }


    public Station updateStation(Long stationId, List<Railway> railways) {
        Station station = getStationById(stationId);
        station.setRailways(railways);
        stationRepository.save(station);
        log.info("Station {} updated", stationId);
        return station;
    }

    public void deleteStation(Long stationId) throws Exception {
        Station station = getStationById(stationId);
        if (station.getRailways().isEmpty()) {
            log.info("station {} deleted", stationId);
            stationRepository.deleteById(stationId);
        } else {
            log.warn("it is impossible to delete the station together with the rails, {}", station.getRailways());
            throw new Exception("Невозможно удалить станцию вместе с рельсами");
        }

    }

    public Station addRailwayToStation(Long stationId, Long railWayId) throws Exception {
        Station station = getStationById(stationId);
        Railway railway = railwayService.getRailwayById(railWayId);
        List<Railway> railways = station.getRailways();
        if (railway.getStatus() == RailwayStatus.Added) throw new Exception("Невозможно добавить путь несколько раз");
        railwayService.setRailwayStatus(railWayId, RailwayStatus.Added);
        railways.add(railway);
        station.setRailways(railways);
        stationRepository.save(station);
        log.info("railway {} added to station {}", railWayId, stationId);
        return station;
    }

    public Station addTrainToStation(Long stationId, Long trainSheetId) {
        Station station = getStationById(stationId);
        TrainSheet train = trainSheetService.getTrainSheetById(trainSheetId);
        List<Wagon> stationUnsortedWagons = station.getUnsortedWagons();
        List<Wagon> trainWagons = train.getWagons();
        trainWagons.forEach(wagon -> {
            if (wagon.getStatus() != WagonStatus.OnStation && wagon.getStatus() != WagonStatus.Unsorted) {
                stationUnsortedWagons.add(wagon);
                wagonService.setWagonStatus(wagon.getWagonNumber(), WagonStatus.Unsorted);
                wagonService.setWagonStation(wagon.getWagonNumber(), stationId);
                wagonService.setWagonLastMove(wagon.getWagonNumber());
            } else {
                try {
                    log.warn("this wagon {} already somewhere added, error in train sheet {}", wagon.getWagonNumber(), trainSheetId);
                    throw new Exception("Этот вагон уже где то стоит, ошибка в натурном листе");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        station.setUnsortedWagons(stationUnsortedWagons);
        train.setStatus(TrainSheetStatus.Archieved);
        stationRepository.save(station);
        log.info("train sheet {} accepted to station {}", trainSheetId, stationId);
        return station;
    }

    public Railway addWagonToStationRailway(Long stationId, Long railWayId) throws Exception {
        Station station = getStationById(stationId);
        Railway railway = railwayService.getRailwayById(railWayId);
        List<Wagon> unsortedWagons = station.getUnsortedWagons();
        if (station.getRailways().contains(railway)) {
            Wagon wagonToAdd = Iterables.getLast(unsortedWagons);
            railwayService.addWagonToRailway(railWayId, wagonToAdd);
            wagonService.setWagonStatus(wagonToAdd.getWagonNumber(), WagonStatus.OnStation);
            wagonService.setWagonRailway(wagonToAdd.getWagonNumber(), railWayId);
            wagonService.setWagonOrderNumber(wagonToAdd.getWagonNumber(), railway.getWagons().indexOf(wagonToAdd) + 1);
            wagonService.setWagonLastMove(wagonToAdd.getWagonNumber());
            unsortedWagons.remove(wagonToAdd);
            station.setUnsortedWagons(unsortedWagons);
            log.info("wagon {} has added to railway {}", wagonToAdd.getWagonNumber(), railway.getId());
            stationRepository.save(station);
            return railway;
        } else {
            log.warn("railway {} not found on this station {}", railWayId, stationId);
            throw new Exception("Такого пути нет на этой станции");
        }

    }


    public Railway removeWagonToStationRailway(Long stationId, Long railWayId) throws Exception {
        Station station = getStationById(stationId);
        Railway railway = railwayService.getRailwayById(railWayId);
        if (station.getRailways().contains(railway)) {
            Wagon wagonToRemove = Iterables.getLast(railway.getWagons());
            railwayService.removeWagonFromRailway(railWayId, wagonToRemove);
            wagonService.setWagonStatus(wagonToRemove.getWagonNumber(), WagonStatus.Leaved);
            wagonService.setWagonStation(wagonToRemove.getWagonNumber(), null);
            wagonService.setWagonRailway(wagonToRemove.getWagonNumber(), null);
            wagonService.setWagonLastMove(wagonToRemove.getWagonNumber());
            railwayService.setWagons(railWayId, wagonService.fixWagonOrder(railway.getWagons()));
            log.info("wagon {} has leaved railway {}", wagonToRemove.getWagonNumber(), railway.getId());
            return railway;
        } else {
            log.warn("railway {} not found on station {}", railway.getId(), stationId);
            throw new Exception("Такого пути нет на этой станции");
        }
    }
}
