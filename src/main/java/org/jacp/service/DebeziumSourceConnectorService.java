package org.jacp.service;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.jacp.config.DebeziumProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Log4j2
public class DebeziumSourceConnectorService {
    @Autowired
    private DebeziumProperties debeziumProperties;
    @Autowired
    private RestTemplate request;
    @Autowired
    private HttpHeaders headers;

    @PostConstruct
    void init() {
        String connectorEndpoint = debeziumProperties.getServerUrl() + debeziumProperties.getConnectorsUrl();
        this.deleteConnector(connectorEndpoint, "debezium-postgres-source-connector");
        this.setUpConfiguration(connectorEndpoint, prepareDefaultConfiguration(debeziumProperties));
    }

    private Map<String, Object> prepareDefaultConfiguration(DebeziumProperties debeziumProperties) {
        debeziumProperties.getConnectorConfig().put("name", "debezium-postgres-source-connector");

        Map<String, Object> config = new HashMap<>();
        config.put("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
        config.put("plugin.name", "pgoutput");
        config.put("database.hostname", "postgres");
        config.put("database.user", "postgres");
        config.put("database.password", "postgres");
        config.put("database.dbname", "processing");
        config.put("database.server.name", "postgres");
        config.put("table.include.list", "public.testdebezium");
        config.put("database.history.kafka.topic", "processing-results");
        config.put("slot.name", "processing_replication_slot_1");

        debeziumProperties.getConnectorConfig().put("config", config);
        return debeziumProperties.getConnectorConfig();
    }

    public void setUpConfiguration(String connectorEndpoint, Map<String, Object> debeziumProperties) {
        Gson json = new Gson();
        String strJson = json.toJson(debeziumProperties);
        log.info("Connector config: " + strJson);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity<String>(strJson, headers);
        try {
            ResponseEntity<String> response = request
                    .exchange(connectorEndpoint,
                            HttpMethod.POST,
                            httpEntity, String.class);
            log.info("Responce: " + response.getBody());
            log.info("Status code: " + response.getStatusCode());
        } catch (Exception e) {
            log.error("Can't create the connector: " + e.getMessage());
        }
    }

    public void deleteConnector(String endpoint, String connectorName) {
        try {
            request.delete(new StringBuilder().append(endpoint)
                    .append("/")
                    .append(connectorName).toString());
            log.info("The connector " + connectorName + " was deleted!");
        } catch (Exception e) {
            log.error("Can't delete the connector " + connectorName + ": " + e.getMessage());
        }
    }

}
