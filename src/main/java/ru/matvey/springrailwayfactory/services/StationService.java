package ru.matvey.springrailwayfactory.services;

import com.google.common.collect.Iterables;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.matvey.springrailwayfactory.enums.WagonStatus;
import ru.matvey.springrailwayfactory.model.Railway;
import ru.matvey.springrailwayfactory.model.Station;
import ru.matvey.springrailwayfactory.model.Wagon;
import ru.matvey.springrailwayfactory.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StationService {
    private final StationRepository stationRepository;
    private final TrainSheetService trainSheetService;
    private final RailwayService railwayService;
    private final WagonService wagonService;

    public Station addStation() {
        var station = Station.builder().railways(new ArrayList<>()).build();
        stationRepository.save(station);
        return station;
    }

    public Station getStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(EntityNotFoundException::new);
    }

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }


    public Station updateStation(Long stationId, List<Railway> railways) {
        var station = getStationById(stationId);
        station.setRailways(railways);
        stationRepository.save(station);
        return station;
    }

    public void deleteStation(Long stationId) throws Exception {
        var station = getStationById(stationId);
        if(station.getRailways().isEmpty()) stationRepository.deleteById(stationId);
        else throw new Exception("Невозможно удалить станцию вместе с рельсами");

    }

    public Station addTrainToStation(Long stationId, Long trainSheetId) {
        var station = getStationById(stationId);
        var train = trainSheetService.getTrainSheetById(trainSheetId);
        List<Wagon> stationUnsortedWagons = station.getUnsortedWagons();
        List<Wagon> trainWagons = train.getWagons();
        trainWagons.forEach(wagon -> {
            if(!stationUnsortedWagons.contains(wagon)) {
                stationUnsortedWagons.add(wagon);
                wagonService.setWagonStatus(wagon.getWagonNumber(), WagonStatus.Unsorted);
                wagonService.setWagonStation(wagon.getWagonNumber(), stationId);
                wagonService.setWagonLastMove(wagon.getWagonNumber());
            } else {
                try {
                    throw new Exception("Нельзя добавить один и тот же вагон несколько раз");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        station.setUnsortedWagons(stationUnsortedWagons);
        stationRepository.save(station);
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
            wagonService.setWagonLastMove(wagonToAdd.getWagonNumber());
            unsortedWagons.remove(wagonToAdd);
            station.setUnsortedWagons(unsortedWagons);
            stationRepository.save(station);
            return railway;
        } else {
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
            return railway;
        } else {
            throw new Exception("Такого пути нет на этой станции");
        }
    }

}
