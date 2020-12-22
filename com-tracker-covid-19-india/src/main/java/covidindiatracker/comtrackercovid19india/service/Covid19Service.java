package covidindiatracker.comtrackercovid19india.service;

import com.amazonaws.services.pinpoint.AmazonPinpoint;
import com.amazonaws.services.pinpoint.AmazonPinpointClientBuilder;
import com.amazonaws.services.pinpoint.model.*;
import covidindiatracker.comtrackercovid19india.domain.Delta;
import covidindiatracker.comtrackercovid19india.domain.District;
import covidindiatracker.comtrackercovid19india.domain.State;
import covidindiatracker.comtrackercovid19india.repo.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            addressMap.put(mobileNumber, new AddressConfiguration().withChannelType(ChannelType.SMS));
            AmazonPinpoint client = AmazonPinpointClientBuilder.standard().withRegion(REGION).build();
            SendMessagesRequest request = new SendMessagesRequest()
                    .withApplicationId(APP_ID)
                    .withMessageRequest(new MessageRequest()
                    .withAddresses(addressMap)
                    .withMessageConfiguration(new DirectMessageConfiguration()
                        .withSMSMessage(new SMSMessage()
                            .withBody(message)
                            .withMessageType(MessageType.TRANSACTIONAL)
                        )
                    )
            );
            client.sendMessages(request);
        }
        catch (Exception ex){
            LOG.error("Message was not sent {}", ex.getMessage());
        }
    }
}
