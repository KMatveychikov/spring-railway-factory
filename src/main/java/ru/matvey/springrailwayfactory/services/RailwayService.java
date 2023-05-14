package ru.matvey.springrailwayfactory.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.matvey.springrailwayfactory.enums.RailwayStatus;
import ru.matvey.springrailwayfactory.model.Railway;
import ru.matvey.springrailwayfactory.model.Wagon;
import ru.matvey.springrailwayfactory.repository.RailwayRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RailwayService {
    private final RailwayRepository railwayRepository;
    private final WagonService wagonService;

    public Railway addRailway() {
        Railway railway = Railway.builder()
                .wagons(new ArrayList<>())
                .status(RailwayStatus.Created)
                .build();
        railwayRepository.save(railway);
        log.info("Railway {} added", railway);
        return railway;
    }
    public Railway getRailwayById(Long railwayId) {
        return railwayRepository.findById(railwayId).orElseThrow(EntityNotFoundException::new);
    }

    public List<Railway> getAllRailways(){
       return railwayRepository.findAll();
    }

    public Railway updateRailway(Long railwayId, List<Wagon> wagons){
        Railway railway =  getRailwayById(railwayId);
        railway.setWagons(wagons);
        railwayRepository.save(railway);
        log.info("Railway {} updated", railway);
        return railway;
    }
    public void setRailwayStatus(Long railwayId, RailwayStatus status){
        Railway railway = getRailwayById(railwayId);
        railway.setStatus(status);
        railwayRepository.save(railway);
        log.info("Railway {} change status to {}", railway, status);
    }

    public void setWagons(Long railwayId, List<Wagon> wagons) {
        Railway railway = getRailwayById(railwayId);
        railway.setWagons(wagons);
        railwayRepository.save(railway);
    }

    public void deleteRailway(Long railwayId) throws Exception {
        Railway railway = getRailwayById(railwayId);
        if(railway.getWagons().isEmpty()) {
            log.info("Delete railway {}", railway);
            railwayRepository.deleteById(railwayId);
        }
        else {
            log.warn("it is impossible to delete this railway because there are wagons on it {}", railway.getWagons());
            throw new Exception("Невозможно удалить рельсы из под вагонов");
        }
    }

    public void addWagonToRailway(Long railwayId, Wagon wagon) {
        Railway railway = getRailwayById(railwayId);
        List<Wagon> wagons = railway.getWagons();
        wagons.add(wagon);
        wagonService.setWagonOrderNumber(wagon.getWagonNumber() ,wagons.indexOf(wagon)+1);
        railway.setWagons(wagons);
        railwayRepository.save(railway);
        log.info("wagon {} added to railway {}", wagon, railway);
    }
    public void removeWagonFromRailway(Long railwayId, Wagon wagon) {
        Railway railway = getRailwayById(railwayId);
        List<Wagon> wagons = railway.getWagons();
        wagons.remove(wagon);
        wagons = wagonService.fixWagonOrder(wagons);
        railway.setWagons(wagons);
        railwayRepository.save(railway);
        log.info("wagon {} removed railway {}", wagon, railway);
    }

}
