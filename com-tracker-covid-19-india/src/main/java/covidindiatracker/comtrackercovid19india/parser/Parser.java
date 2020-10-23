package covidindiatracker.comtrackercovid19india.parser;

import covidindiatracker.comtrackercovid19india.domain.Delta;
import covidindiatracker.comtrackercovid19india.domain.District;
import covidindiatracker.comtrackercovid19india.domain.State;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;


@Component
public class Parser {

    private static final String STATE_CODE = "statecode";
    private static final String ACTIVE = "active";
    private static final String CONFIRMED = "confirmed";
    private static final String RECOVERED = "recovered";
    private static final String DECEASED = "deceased";
    private static final String NOTES = "notes";
    private static final String DELTA = "delta";
    private static final String DISTRICT_DATA = "districtData";



    private static final Logger LOG = LoggerFactory.getLogger(Parser.class);

    public Set<State> parse (String json) {
        Objects.requireNonNull(json,"Incoming JSON message is null");
        Set<State> states = new HashSet<>();
        JSONObject jsonObject = new JSONObject(json);
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()){
            State state = new State();
            String stateName = keys.next();
            LOG.info("Received request for {}", stateName);
            state.setStateName(stateName);
            JSONObject stateObject = ((JSONObject) jsonObject.get(stateName));
            state.setStateCode(stateObject.get(STATE_CODE).toString());
            if (jsonObject.get(stateName) instanceof JSONObject){
                state.setDistricts(buildDistrictData((JSONObject)stateObject.get(DISTRICT_DATA)));
                states.add(state);
            }
        }
        return states;
    }

    private Set<District> buildDistrictData(JSONObject districtData){
        Set<District> districtSet = new HashSet<>();
        if (Objects.nonNull(districtData)){
            Iterator<String> districts = districtData.keys();
            while (districts.hasNext()){
                String districtName = districts.next();
                LOG.info("Beginning to build district data for {}", districtName);
                JSONObject districtJson = ((JSONObject)districtData.get(districtName));
                District district = District.builder()
                        .districtName(districtName)
                        .active(Integer.valueOf(districtJson.get(ACTIVE).toString()))
                        .confirmed(Integer.valueOf(districtJson.get(CONFIRMED).toString()))
                        .recovered(Integer.valueOf(districtJson.get(RECOVERED).toString()))
                        .deceased(Integer.valueOf(districtJson.get(DECEASED).toString()))
                        .notes(districtJson.get(NOTES).toString())
                        .delta(buildDeltaObject((JSONObject)districtJson.get(DELTA)))
                        .build();
                districtSet.add(district);
            }
        }
        return districtSet;
    }

    private Delta buildDeltaObject(JSONObject deltaObject){
        return Delta.builder()
                .recovered(Integer.valueOf(deltaObject.get(RECOVERED).toString()))
                .confirmed(Integer.valueOf(deltaObject.get(CONFIRMED).toString()))
                .deceased(Integer.valueOf(deltaObject.get(DECEASED).toString()))
                .build();
    }

}
