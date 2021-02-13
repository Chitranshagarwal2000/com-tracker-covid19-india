package covidindiatracker.comtrackercovid19india.repo;

import covidindiatracker.comtrackercovid19india.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DistrictRepository extends JpaRepository<District, Long> {

    @Query(value = "select * from district where district_name = :districtName and state_id = (select state_id from state where state_name = :stateName)", nativeQuery = true)
    District findByDistrictNameAndStateName(@Param("districtName") String districtName,
                                                       @Param("stateName") String stateName);

    String findByDistrictName(String districtName);
}
