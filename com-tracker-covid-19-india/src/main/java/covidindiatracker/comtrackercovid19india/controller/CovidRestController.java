package covidindiatracker.comtrackercovid19india.controller;

import covidindiatracker.comtrackercovid19india.domain.Delta;
import covidindiatracker.comtrackercovid19india.domain.District;
import covidindiatracker.comtrackercovid19india.domain.State;
import covidindiatracker.comtrackercovid19india.domain.User;
import covidindiatracker.comtrackercovid19india.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.el.EvaluationListener;
import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
public class CovidRestController {

    @Value("${api.new-user-save-format}")
    private String newUserSaveFormat;

    private RestDataExtractorService restDataExtractorService;
    private Covid19Service covid19Service;
    private UserService userService;
    private DistrictService districtService;
    private DeltaService deltaService;
    private static final Logger LOG = LoggerFactory.getLogger(CovidRestController.class);

    @Autowired
    public CovidRestController(RestDataExtractorService restDataExtractorService, Covid19Service covid19Service, UserService userService, DistrictService districtService, DeltaService deltaService){
        this.restDataExtractorService = restDataExtractorService;
        this.covid19Service = covid19Service;
        this.userService = userService;
        this.districtService = districtService;
        this.deltaService = deltaService;
    }

    @RequestMapping(value = "/fetchAndSave")
    public ResponseEntity fetchDataAndSave(){
        Set<State> states = restDataExtractorService.returnObjectsFromApi();
        if (CollectionUtils.isEmpty(states)){
            LOG.error("Unable to fetch the data of states, please check API");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to save data of states, please check the logs");
        }
        covid19Service.save(states);
        sendMessagesToUsers();
        return ResponseEntity.status(HttpStatus.OK).body("Data saved successfully!!");
    }

    @RequestMapping(value = "/newuser")
    public ResponseEntity<String> persistNewUser(@RequestParam String name,
                                               @RequestParam String number,
                                               @RequestParam String state,
                                               @RequestParam String district){
        try {
            number = number.length() == 10
                    ? "+91" + number
                    : number.length() == 13 && StringUtils.startsWithIgnoreCase("+91", number)
                    ? number
                    : null;
            if (Objects.isNull(number))
                throw new IllegalArgumentException("Please enter a number belonging to India");
            LOG.info("User received with name [{}], number [{}], state [{}] and district [{}]", name,number,state,district);
            User user = User.builder()
                    .username(name)
                    .mobileNumber(number)
                    .state(state)
                    .district(district)
                    .build();
            User existingUser = userService.fetchUsersFromDB(number);
            if (Objects.nonNull(existingUser)){
                user.setUserId(existingUser.getUserId());
                throw new EntityExistsException("User with mobileNumber" + number + " already exists");
            }
            userService.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(user.toString());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.toString());
        }

    }

    @RequestMapping(value = "/newusersaveformat")
    public ResponseEntity displayNewUserSaveFormat(){
        return Objects.nonNull(newUserSaveFormat)
                ? ResponseEntity.status(HttpStatus.OK).body(newUserSaveFormat)
                : ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("No Format is defined");
    }

    @RequestMapping(value = "/sendMessages")
    public ResponseEntity sendMessagesToUsers(){
        List<User> users = userService.findAll();
        if (CollectionUtils.isEmpty(users)){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No user exist in the db");
        }
        users.forEach(user -> {
            String mobileNumber = user.getMobileNumber();
            String stateName = user.getState();
            String districtName = user.getDistrict();
            District district = districtService.findDistrictByDistrictNameAndStateName(districtName, stateName);
            if (Objects.nonNull(district)){
                Delta delta = deltaService.findDeltaByDistrictId(district.getDistrictId());
                if (delta.getConfirmed() > 0 || delta.getDeceased() > 0 || delta.getRecovered() > 0) {
                    String message = "Current attributes are :-"
                            + System.getProperty("line.separator") + "Total: " + district.getConfirmed()
                            + System.getProperty("line.separator") + "Active: " + district.getActive() + "( " + delta.getConfirmed() + ")"
                            + System.getProperty("line.separator") + "Recovered: " + district.getRecovered() + ("(" + delta.getRecovered() + ")"
                            + System.getProperty("line.separator") + "Deceased: " + district.getDeceased() + "(" + delta.getDeceased() + ")");
                    LOG.debug("Message generated: {}", message);
                    covid19Service.sendSms(mobileNumber, message);
                }
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body("Request successful");
    }
}
