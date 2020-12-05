package covidindiatracker.comtrackercovid19india.repo;

import covidindiatracker.comtrackercovid19india.domain.Delta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeltaRepository extends JpaRepository<Delta, Long> {
    Delta findByDistrictId(Long districtId);
}
