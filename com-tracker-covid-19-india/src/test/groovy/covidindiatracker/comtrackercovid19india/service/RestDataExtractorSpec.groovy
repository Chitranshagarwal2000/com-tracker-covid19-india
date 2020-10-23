package covidindiatracker.comtrackercovid19india.service

import covidindiatracker.comtrackercovid19india.Application
import covidindiatracker.comtrackercovid19india.domain.State
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Narrative
import spock.lang.Specification

@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Narrative("Need to check the data is getting fetched from the url and is getting parsed")
class RestDataExtractorSpec extends Specification {

    @Autowired
    RestDataExtractorService restDataExtractor

    def "Initial Test"(){
        when:
        Set<State> states = restDataExtractor.returnObjectsFromApi();

        then:
        println(states)
    }
}
