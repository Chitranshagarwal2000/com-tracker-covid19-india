package covidindiatracker.comtrackercovid19india.service;

import com.google.common.collect.ImmutableMap;
import covidindiatracker.comtrackercovid19india.domain.Delta;
import covidindiatracker.comtrackercovid19india.domain.District;
import covidindiatracker.comtrackercovid19india.domain.State;
import covidindiatracker.comtrackercovid19india.repo.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Covid19Service {

    private static final Logger LOG = LoggerFactory.getLogger(Covid19Service.class);

    private StateRepository stateRepository;

    @Autowired
    public Covid19Service(StateRepository stateRepository){
        this.stateRepository = stateRepository;
    }

    public void save(Set<State> statesToSave){
        try {
            statesToSave.forEach(stateToSave -> {
                State existingState = stateRepository.findByStateName(stateToSave.getStateName());
                if (Objects.nonNull(existingState)){
                    LOG.debug("State[{}] already exist in DB, tagging stateId {} to it", existingState.getStateName(),existingState.getStateId());
                    stateToSave.setStateId(existingState.getStateId());
                    stateToSave.setDistricts(tagDistrictId(stateToSave.getDistricts(), existingState.getDistricts()));
                }
                stateRepository.save(stateToSave);
            });
        } catch (Exception e) {
            LOG.error("Exception occurred while saving data", e);
        }
    }


    private Set<District> tagDistrictId(Set<District> districtsToSave, Set<District> existingDistricts){
        Set<District> districtSet = new HashSet<>();

        Map<String,Map<Long,Delta>> districtNameIdMap = existingDistricts.stream()
                .collect(Collectors.groupingBy(District::getDistrictName, Collectors.toMap(District::getDistrictId, District::getDelta)));

        districtsToSave.forEach(districtToSave -> {
            if (districtNameIdMap.containsKey(districtToSave.getDistrictName())){
                districtToSave.setDistrictId(districtNameIdMap.get(districtToSave.getDistrictName()).keySet().iterator().next());
                Delta deltaToSave = districtToSave.getDelta();
                deltaToSave.setDeltaId(districtNameIdMap.get(districtToSave.getDistrictName()).values().iterator().next().getDeltaId());
                districtToSave.setDelta(deltaToSave);
            }
            districtSet.add(districtToSave);
        });
        return districtSet;
    }
}
