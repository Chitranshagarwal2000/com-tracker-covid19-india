package covidindiatracker.comtrackercovid19india.service;

import covidindiatracker.comtrackercovid19india.domain.User;
import covidindiatracker.comtrackercovid19india.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User fetchUsersFromDB(Long mobile){
        return userRepository.findByMobileNumber(mobile);
    }

    public Set<User> findAll(){
        Set<User> users = new HashSet<>();
        try {
            users = (Set<User>) userRepository.findAll();
            if (CollectionUtils.isEmpty(users)){
                LOG.error("No data returned from the DB, please verify whether any existing users exist");
            }
        } catch (Exception e) {
            LOG.error("Exception caught while fetching users from DB {}", e);
        }
        return users;
    }

    public void save(User user){
        try {
            if (Objects.nonNull(user)){
                userRepository.save(user);
            }
        } catch (Exception e) {
            LOG.error("Error occurred in saving user {} to the DB {}", user, e);
        }
    }
}