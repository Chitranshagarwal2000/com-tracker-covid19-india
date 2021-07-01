package covidindiatracker.comtrackercovid19india.service;

import com.google.common.collect.Sets;
import covidindiatracker.comtrackercovid19india.domain.State;
import covidindiatracker.comtrackercovid19india.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public class RestDataExtractorService {

    @Value("${api.state-wise-url-json}")
    private String url;

    @Autowired
    private final Parser parser;

    private static final Logger LOG = LoggerFactory.getLogger(RestDataExtractorService.class);

    public RestDataExtractorService(Parser parser) {
        this.parser = parser;
    }

    public Set<State> returnObjectsFromApi(){
        LOG.info("Beginning to fetch the data from url {}", url);
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url,String.class);
            return parser.parse(response.getBody());
        } catch (Exception e) {
            LOG.error("Unable to fetch data from url {} due to {}", url, e);
            return Sets.newHashSet();
        }
    }
}
