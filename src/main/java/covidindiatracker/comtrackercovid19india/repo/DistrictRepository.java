package covidindiatracker.comtrackercovid19india.repo;

import covidindiatracker.comtrackercovid19india.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DistrictRepository extends JpaRepository<District, Long> {

    @Query(value = "SELECT d from District d inner join State s on s.stateId = d.stateId where s.stateName = :stateName and d.districtName = :districtName")
    District findByDistrictNameAndStateName(@Param("districtName") String districtName,
                                            @Param("stateName") String stateName);

}
