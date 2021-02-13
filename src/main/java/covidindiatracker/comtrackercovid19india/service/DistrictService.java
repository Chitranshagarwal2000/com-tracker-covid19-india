package covidindiatracker.comtrackercovid19india.service;

import covidindiatracker.comtrackercovid19india.domain.District;
import covidindiatracker.comtrackercovid19india.repo.DistrictRepository;
import groovy.transform.AutoClone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistrictService {

    @Autowired
    private DistrictRepository districtRepository;

    private static final Logger LOG = LoggerFactory.getLogger(DistrictService.class);

    public District findDistrictByDistrictNameAndStateName (String districtName, String stateName){
        return districtRepository.findByDistrictNameAndStateName(districtName, stateName);
    }

}
