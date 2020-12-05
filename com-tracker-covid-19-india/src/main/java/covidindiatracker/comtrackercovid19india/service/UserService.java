package covidindiatracker.comtrackercovid19india.service;

import covidindiatracker.comtrackercovid19india.domain.District;
import covidindiatracker.comtrackercovid19india.domain.State;
import covidindiatracker.comtrackercovid19india.domain.User;
import covidindiatracker.comtrackercovid19india.repo.DistrictRepository;
import covidindiatracker.comtrackercovid19india.repo.StateRepository;
import covidindiatracker.comtrackercovid19india.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {

    private UserRepository userRepository;
    private StateRepository stateRepository;
    private DistrictRepository districtRepository;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, StateRepository stateRepository, DistrictRepository districtRepository){
        this.userRepository = userRepository;
        this.stateRepository = stateRepository;
        this.districtRepository = districtRepository;
    }

    public User fetchUsersFromDB(String mobile){
        return userRepository.findByMobileNumber(mobile);
    }

    public List<User> findAll(){
        List<User> users = new ArrayList<>();
        try {
            users = userRepository.findAll();
            if (CollectionUtils.isEmpty(users)){
                LOG.error("No data returned from the DB, please verify whether any existing users exist");
            }
        } catch (Exception e) {
            LOG.error("Exception caught while fetching users from DB {}", e.getMessage());
        }
        return users;
    }

    public void save(User user) {
        if (Objects.nonNull(user)) {
            District district = districtRepository.findByDistrictNameAndStateName(user.getDistrict(), user.getState());
            if (!StringUtils.isEmpty(district)) {
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("Combination of State " + user.getState() + " and district " + user.getDistrict() + " does not exist");
            }
        }

    }
}