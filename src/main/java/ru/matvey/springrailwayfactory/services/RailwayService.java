package ru.matvey.springrailwayfactory.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.matvey.springrailwayfactory.model.Railway;
import ru.matvey.springrailwayfactory.model.Wagon;
import ru.matvey.springrailwayfactory.repository.RailwayRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RailwayService {
    private final RailwayRepository railwayRepository;

    public Railway addRailway() {
        var railway = Railway.builder()
                .wagons(new ArrayList<>())
                .build();
        railwayRepository.save(railway);
        return railway;
    }
    public Railway getRailwayById(Long railwayId) {
        return railwayRepository.findById(railwayId).orElseThrow(EntityNotFoundException::new);
    }

    public List<Railway> getAllRailways(){
       return railwayRepository.findAll();
    }

    public Railway updateRailway(Long railwayId, List<Wagon> wagons){
        var railway =  getRailwayById(railwayId);
        railway.setWagons(wagons);
        railwayRepository.save(railway);
        return railway;
    }

    public void deleteRailway(Long railwayId) throws Exception {
        var railway = getRailwayById(railwayId);
        if(railway.getWagons().isEmpty()) railwayRepository.deleteById(railwayId);
        else throw new Exception("Невозможно удалить рельсы из под вагонов");
    }

    public void addWagonToRailway(Long railwayId, Wagon wagon) {
        var railway = getRailwayById(railwayId);
        List<Wagon> wagons = railway.getWagons();
        wagons.add(wagon);
        railway.setWagons(wagons);
        railwayRepository.save(railway);
    }
    public void removeWagonFromRailway(Long railwayId, Wagon wagon) {
        var railway = getRailwayById(railwayId);
        List<Wagon> wagons = railway.getWagons();
        wagons.remove(wagon);
        railway.setWagons(wagons);
        railwayRepository.save(railway);
    }

}
