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

    private static final Logger LOG = LoggerFactory.getLogger(Parser.class);

    public void parse (String json) {
        Objects.requireNonNull(json,"Incoming JSON message is null");
        Set<State> states = new HashSet<>();
        JSONObject jsonObject = new JSONObject(json);
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()){
            State state = new State();
            String stateName = keys.next();
            state.setStateName(stateName);
            state.setStateCode(((JSONObject) jsonObject.get(stateName)).get("statecode").toString());
            if (jsonObject.get(stateName) instanceof JSONObject){
                state.setDistricts(buildDistrictData((JSONObject)((JSONObject) jsonObject.get(stateName)).get("districtData")));
                states.add(state);
            }
        }
    }

    private Set<District> buildDistrictData(JSONObject districtData){
        Set<District> districtSet = new HashSet<>();
        if (Objects.nonNull(districtData)){
            LOG.info("Beginning to build district data");
            Iterator<String> districts = districtData.keys();
            while (districts.hasNext()){
                String districtName = districts.next();
                District district = District.builder()
                        .districtName(districtName)
                        .active(Integer.getInteger(((JSONObject)districtData.get(districtName)).get("active").toString()))
                        .confirmed(Integer.getInteger(((JSONObject)districtData.get(districtName)).get("confirmed").toString()))
                        .recovered(Integer.getInteger(((JSONObject)districtData.get(districtName)).get("recovered").toString()))
                        .deceased(Integer.getInteger(((JSONObject)districtData.get(districtName)).get("deceased").toString()))
                        .notes(((JSONObject)districtData.get(districtName)).get("notes").toString())
                        .delta(buildDeltaObject((JSONObject)((JSONObject) districtData.get(districtName)).get("delta")))
                        .build();
                districtSet.add(district);
            }
        }
        return districtSet;
    }

    private Delta buildDeltaObject(JSONObject deltaObject){
        return null;
    }

}
