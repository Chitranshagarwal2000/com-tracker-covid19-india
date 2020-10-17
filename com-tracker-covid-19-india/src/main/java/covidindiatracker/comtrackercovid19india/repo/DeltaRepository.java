package covidindiatracker.comtrackercovid19india.repo;

import covidindiatracker.comtrackercovid19india.domain.Delta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeltaRepository extends JpaRepository<Delta, Long> {
}
