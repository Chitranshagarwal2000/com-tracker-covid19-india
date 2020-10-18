package covidindiatracker.comtrackercovid19india.service;

import com.google.common.collect.ImmutableMap;
import covidindiatracker.comtrackercovid19india.domain.Delta;
import covidindiatracker.comtrackercovid19india.domain.District;
import covidindiatracker.comtrackercovid19india.domain.State;
import covidindiatracker.comtrackercovid19india.repo.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class Covid19Service {

    private static final Logger LOG = LoggerFactory.getLogger(Covid19Service.class);

    private StateRepository stateRepository;

    @Autowired
    public Covid19Service(StateRepository stateRepository){
        this.stateRepository = stateRepository;
    }

    public void save(State stateToSave){
        State existingState = stateRepository.findByStateName(stateToSave.getStateName());
        if (Objects.nonNull(existingState)){
            LOG.debug("State[{}] already exist in DB, tagging stateId {} to it", existingState.getStateName(),existingState.getStateId());
            stateToSave.setStateId(existingState.getStateId());
            stateToSave.setDistricts(tagDistrictId(stateToSave.getDistricts(), existingState.getDistricts()));
        }
        stateRepository.save(stateToSave);
    }

    private Set<District> tagDistrictId(Set<District> districtsToSave, Set<District> existingDistricts){
        Map<String,Map<Long,Long>> map = existingDistricts.stream()
                .filter(districtsToSave::contains)
                .collect(Collectors.toMap(District::getDistrictName, t -> ImmutableMap.of(t.getDistrictId(), t.getDelta().getDeltaId())));

        districtsToSave.forEach(t -> {
            String name = t.getDistrictName();
            Delta delta = t.getDelta();
            delta.setDeltaId(map.get(name).entrySet().iterator().next().getValue());
            t.setDelta(delta);
            t.setDistrictId(map.get(name).entrySet().iterator().next().getKey());
        });

        return districtsToSave;

    }
}
