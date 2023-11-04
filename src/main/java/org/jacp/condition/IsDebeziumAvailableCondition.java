package org.jacp.condition;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class IsDebeziumAvailableCondition implements Condition {
    private String serverUrl;
    private String connectorsUrl;
    private String endPoint;

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            serverUrl = context.getEnvironment().getProperty("debezium.serverUrl");
            connectorsUrl = context.getEnvironment().getProperty("debezium.connectorsUrl");
            endPoint = serverUrl + connectorsUrl;
            ResponseEntity<String> response = new RestTemplate().getForEntity(endPoint, String.class);
            if (!response.getStatusCode().is2xxSuccessful())
                throw new Exception();
            log.info("Debezium server " + endPoint + " is available");
        } catch (Exception e) {
            log.error("Debezium server " + endPoint + " isn't available");
            return false;
        }
        return true;
    }
}
