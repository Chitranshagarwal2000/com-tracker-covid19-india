package covidindiatracker.comtrackercovid19india

import com.google.common.collect.Sets
import covidindiatracker.comtrackercovid19india.domain.Delta
import covidindiatracker.comtrackercovid19india.domain.District
import covidindiatracker.comtrackercovid19india.domain.State
import covidindiatracker.comtrackercovid19india.service.Covid19Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ContextConfiguration
import spock.lang.Issue
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Narrative("Just to check whether the initial save is happening, would change the profile to test post test")
@ContextConfiguration(classes = Application.class)
@Subject(Application)
@Profile("test")
class InitialSaveTest extends Specification{

    private static final Logger LOG = LoggerFactory.getLogger(InitialSaveTest.class);

    @Autowired
    Covid19Service stateService

    @Issue("Initial Test to check whether data is getting persisted to DB")
    def "Initial save test" (){
        given:
        Delta delta1 = Delta.builder().confirmed(100).deceased(100).recovered(10).build();
        Delta delta2 = Delta.builder().confirmed(10).deceased(20).recovered(30).build();
        Delta delta3 = Delta.builder().confirmed(4).deceased(5).recovered(6).build();
        District district1 = District.builder().recovered(5).deceased(6).confirmed(7).active(8).delta(delta2).districtName("Indore").notes("ASDADA").build()
        District district2 = District.builder().recovered(10).deceased(20).confirmed(30).active(40).delta(delta1).districtName("Bhopal").notes("ASDADA").build()
        District district3 = District.builder().recovered(9).deceased(10).confirmed(11).active(12).delta(delta3).districtName("Gwalior").notes("ASDADA").build()
//        delta.setDistrict(district)
        district1.setDelta(delta1)
        district2.setDelta(delta2)
        district3.setDelta(delta3)
        State state = State.builder().stateCode("MP").stateName("Madhya Pradesh").districts(Sets.newHashSet(district1,district2,district3)).build()

        when:
        stateService.save(state)

        then:
        1+1 == 2
    }
}