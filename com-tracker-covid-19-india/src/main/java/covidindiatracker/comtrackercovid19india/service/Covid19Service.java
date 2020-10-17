package covidindiatracker.comtrackercovid19india.service;

import covidindiatracker.comtrackercovid19india.domain.District;
import covidindiatracker.comtrackercovid19india.domain.State;
import covidindiatracker.comtrackercovid19india.repo.DeltaRepository;
import covidindiatracker.comtrackercovid19india.repo.DistrictRepository;
import covidindiatracker.comtrackercovid19india.repo.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class Covid19Service {

    private StateRepository stateRepository;
    private DistrictRepository districtRepository;
    private DeltaRepository deltaRepository;

    @Autowired
    public Covid19Service(StateRepository stateRepository, DeltaRepository deltaRepository, DistrictRepository districtRepository){
        this.stateRepository = stateRepository;
        this.districtRepository = districtRepository;
        this.deltaRepository = deltaRepository;
    }

    public void save(State stateToSave){
        State existingState = stateRepository.findByStateName(stateToSave.getStateName());
        if (Objects.nonNull(existingState)){
            stateToSave.setStateId(existingState.getStateId());
            stateToSave.setDistricts(tagDistrictId(stateToSave.getDistricts(), districtRepository.findAllByStateId(existingState.getStateId())));
        }
        stateRepository.save(stateToSave);
    }

    private Set<District> tagDistrictId(Set<District> districtsToSave, Set<District> existingDistricts){
        Map<String,Long> map = existingDistricts.stream()
                .filter(districtsToSave::contains)
                .collect(Collectors.toMap(District::getDistrictName, District::getDistrictId));

        districtsToSave.forEach(t -> {
            String name = t.getDistrictName();
            t.setDistrictId(map.get(name));
        });
        return districtsToSave;
    }

}
