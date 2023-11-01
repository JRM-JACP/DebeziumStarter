package org.jacp.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.jacp.condition.IsDebeziumAvailableCondition;
import org.jacp.service.DebeziumSourceConnectorService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(DebeziumProperties.class)
@Conditional(IsDebeziumAvailableCondition.class)
@Log4j2
public class DebeziumAutoConfiguration {
    @PostConstruct
    void init() {
        log.info("Debezium starter was connected to project");
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpHeaders getHttpHeaders() {
        return new HttpHeaders();
    }

    @Bean
    public DebeziumSourceConnectorService getSetUpDebeziumSourceConnector() {
        return new DebeziumSourceConnectorService();
    }
}
