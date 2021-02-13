package covidindiatracker.comtrackercovid19india.credentialprovider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.DecryptionFailureException;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.utils.StringUtils;

import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

@Slf4j
public class DatabasePropertiesListener implements ApplicationListener<ApplicationPreparedEvent> {

    private final static String SPRING_DATASOURCE_USERNAME = "spring.datasource.username";
    private final static String SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";
    private static final String SECRET_NAME = "database/credentials";
    private static final String REGION = "ap-south-1";
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        String secretJson = getSecret();
        String dbUser = getString(secretJson, "username");
        String dbPassword = getString(secretJson, "password");
        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
        Properties props = new Properties();
        props.put(SPRING_DATASOURCE_USERNAME, dbUser);
        props.put(SPRING_DATASOURCE_PASSWORD, dbPassword);
        environment.getPropertySources().addFirst(new PropertiesPropertySource("aws.secret.manager",props));
    }

    private String getSecret() {
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.of(REGION))
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(SECRET_NAME)
                .build();
        GetSecretValueResponse getSecretValueResponse = null;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        }
        catch (Exception e){
            log.error("Exception occured {}",e.getMessage());
        }
        if (!StringUtils.isEmpty(getSecretValueResponse.secretString())){
            return getSecretValueResponse.secretString();
        }
        else {
            return new String(Base64.getDecoder().decode(getSecretValueResponse.secretBinary().asByteBuffer()).array());
        }
    }

    private String getString(String json, String path){
        try {
            JsonNode root = mapper.readTree(json);
            return root.path(path).asText();
        } catch (IOException e) {
            log.error("Cant get {} from json {}", path, json, e);
            return null;
        }
    }
}
