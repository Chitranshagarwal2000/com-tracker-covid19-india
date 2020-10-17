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
import org.springframework.test.context.ContextConfiguration
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Narrative("Just to check whether the initial save is happening, would change the profile to test post test")
@ContextConfiguration(classes = Application.class)
@Subject(Application)
class InitialSaveTest extends Specification{

    private static final Logger LOG = LoggerFactory.getLogger(InitialSaveTest.class);

    @Autowired
    Covid19Service stateService

    def "Initial save test" (){
        given:
        Delta delta = Delta.builder().confirmed(10).deceased(10).recovered(10).build();
        District district = District.builder().recovered(10).deceased(10).confirmed(10).active(10).delta(delta).districtName("Bhopal").notes("ASDADA").build()
        State state = State.builder().stateCode("MP").stateName("Madhya Pradesh").districts(Sets.newHashSet(district)).build()
        LOG.info("Printing")

        when:
        stateService.save(state)
//        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//        Session session = sessionFactory.openSession();
//        session.beginTransaction()
//        session.save(state)
//        session.getTransaction().commit()
//        session.close()

        then:
        1+1 == 2
    }
}