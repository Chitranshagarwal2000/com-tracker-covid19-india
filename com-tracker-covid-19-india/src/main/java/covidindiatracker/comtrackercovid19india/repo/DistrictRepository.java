package covidindiatracker.comtrackercovid19india.repo;

import covidindiatracker.comtrackercovid19india.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    Set<District> findAllByStateId(Long stateId);
}
