package covidindiatracker.comtrackercovid19india.repo;

import covidindiatracker.comtrackercovid19india.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByMobileNumber(String mobilenumber);
}
