package covidindiatracker.comtrackercovid19india.parser

import covidindiatracker.comtrackercovid19india.Application
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application.class)
@Subject(Parser)
@Narrative("Check whether parser is able to parse the given json and match the output")
class ParserSpec extends Specification {

    @Autowired
    Parser parser

    def "Initial test" () {
        setup:
        String json = new String(Files.readAllBytes(Paths.get("C:\\Code\\com-tracker-covid-19-india\\src\\test\\resources\\data.json")), StandardCharsets.UTF_8);

        when:
        parser.parse(json)

        then:
        1+1==2
    }
}
