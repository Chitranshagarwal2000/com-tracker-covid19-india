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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Covid19Service {

    private static final Logger LOG = LoggerFactory.getLogger(Covid19Service.class);

    private static final String REGION = "ap-south-1";
    private static final String APP_ID = "61710ed3063f49e59180b6586ce02cd1";


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
    public void sendSms(String mobileNumber, String message){
        try {
            Map<String, AddressConfiguration> addressMap = new HashMap<>();
            AddressConfiguration addConfig = AddressConfiguration.builder()
                    .channelType(ChannelType.SMS)
                    .build();
            addressMap.put(mobileNumber, addConfig);
            SMSMessage smsMessage = SMSMessage.builder()
                    .body(message)
                    .messageType(MessageType.TRANSACTIONAL)
                    .build();

            DirectMessageConfiguration direct = DirectMessageConfiguration.builder()
                    .smsMessage(smsMessage)
                    .build();

            MessageRequest messageRequest = MessageRequest.builder()
                    .addresses(addressMap)
                    .messageConfiguration(direct)
                    .build();

            SendMessagesRequest request = SendMessagesRequest.builder()
                    .applicationId(APP_ID)
                    .messageRequest(messageRequest)
                    .build();

            PinpointClient pinpointClient = PinpointClient.builder()
                    .region(Region.AP_SOUTH_1)
                    .build();

            SendMessagesResponse response = pinpointClient.sendMessages(request);

            MessageResponse response1 = response.messageResponse();
            Map<String, MessageResult> map = response1.result();

            map.forEach((k, v) -> LOG.debug(k + ":" +v));
        }
        catch (PinpointException ex){
            LOG.error("Message was not sent {}", ex.getMessage());
        }
    }
}
