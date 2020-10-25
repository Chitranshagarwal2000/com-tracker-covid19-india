package covidindiatracker.comtrackercovid19india.controller;

import covidindiatracker.comtrackercovid19india.domain.State;
import covidindiatracker.comtrackercovid19india.domain.User;
import covidindiatracker.comtrackercovid19india.service.Covid19Service;
import covidindiatracker.comtrackercovid19india.service.RestDataExtractorService;
import covidindiatracker.comtrackercovid19india.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityExistsException;
import java.util.Objects;
import java.util.Set;

@RestController
public class CovidRestController {

    private RestDataExtractorService restDataExtractorService;
    private Covid19Service covid19Service;
    private UserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(CovidRestController.class);

    @Autowired
    public CovidRestController(RestDataExtractorService restDataExtractorService, Covid19Service covid19Service, UserService userService){
        this.restDataExtractorService = restDataExtractorService;
        this.covid19Service = covid19Service;
        this.userService = userService;
    }

    @RequestMapping(value = "/fetchAndSave")
    public ResponseEntity fetchDataAndSave(){
        Set<State> states = restDataExtractorService.returnObjectsFromApi();
        if (CollectionUtils.isEmpty(states)){
            LOG.error("Unable to fetch the data of states, please check API");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to save data of states, please check the logs");
        }
        covid19Service.save(states);
        return ResponseEntity.status(HttpStatus.OK).body("Data saved successfully!!");
    }

    @RequestMapping(value = "/newuser")
    public ResponseEntity<User> persistNewUser(@RequestParam String name,
                                               @RequestParam String number,
                                               @RequestParam String state,
                                               @RequestParam String district){
        try {
            LOG.info("User received with name [{}], number [{}], state [{}] and district [{}]", name,number,state,district);
            User user = User.builder()
                    .username(name)
                    .mobileNumber(Long.valueOf(number))
                    .state(state)
                    .district(district)
                    .build();
            User existingUser = userService.fetchUsersFromDB(Long.valueOf(number));
            if (Objects.nonNull(existingUser)){
                user.setUserId(existingUser.getUserId());
                throw new EntityExistsException("User with mobileNumber" + number + "already exists");
            }
            userService.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

    }
}
