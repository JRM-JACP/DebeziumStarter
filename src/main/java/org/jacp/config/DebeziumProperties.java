package org.jacp.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "debezium")
@Log4j2
public class DebeziumProperties {
    private String serverUrl;
    private String connectorsUrl;
    private Map<String, Object> connectorConfig = new HashMap<>();

    public Map<String, Object> getConnectorConfig() {
        return connectorConfig;
    }

    @PostConstruct
    void init() {
        log.info("Debezium properties: " + this);
    }
}
