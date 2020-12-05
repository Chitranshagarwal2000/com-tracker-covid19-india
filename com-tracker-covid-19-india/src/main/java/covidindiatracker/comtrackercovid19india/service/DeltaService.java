package covidindiatracker.comtrackercovid19india.service;

import covidindiatracker.comtrackercovid19india.domain.Delta;
import covidindiatracker.comtrackercovid19india.repo.DeltaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeltaService {

    @Autowired
    private DeltaRepository deltaRepository;

    public Delta findDeltaByDistrictId(Long districtId){
        return deltaRepository.findByDistrictId(districtId);
    }
}
