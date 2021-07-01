package covidindiatracker.comtrackercovid19india.service;

import covidindiatracker.comtrackercovid19india.domain.Delta;
import covidindiatracker.comtrackercovid19india.domain.District;
import covidindiatracker.comtrackercovid19india.domain.State;
import covidindiatracker.comtrackercovid19india.repo.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Covid19Service {

    private static final Logger LOG = LoggerFactory.getLogger(Covid19Service.class);


    private final StateRepository stateRepository;

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

        Map<String,Long> districtNameIdMap = existingDistricts.stream()
                .collect(Collectors.toMap(District::getDistrictName, District::getDistrictId));

        districtsToSave.forEach(districtToSave -> {
            if (districtNameIdMap.containsKey(districtToSave.getDistrictName())){
                districtToSave.setDistrictId(districtNameIdMap.get(districtToSave.getDistrictName()));
                Delta deltaToSave = districtToSave.getDelta();
                deltaToSave.setDistrictId(districtNameIdMap.get(districtToSave.getDistrictName()));
                districtToSave.setDelta(deltaToSave);
            }
            districtSet.add(districtToSave);
        });
        return districtSet;
    }
    public void sendSmsMessages(SnsClient client, String message, String phoneNumber) {
        PublishRequest request = PublishRequest.builder().message(message).phoneNumber(phoneNumber).build();
        PublishResponse publishResponse = client.publish(request);
        String messageId = publishResponse.messageId();
        LOG.info(messageId + " Message sent. Status was " + publishResponse.sdkHttpResponse().statusCode());
    }
}
