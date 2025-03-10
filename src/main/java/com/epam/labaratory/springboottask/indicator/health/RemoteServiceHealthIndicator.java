package com.epam.labaratory.springboottask.indicator.health;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RemoteServiceHealthIndicator implements HealthIndicator {

    RestTemplate restTemplate = new RestTemplate();
    static String REMOTE_SERVICE_URL = "https://remote-service-url/health";

    @Override
    public Health health() {
        try {
            String response = restTemplate.getForObject(REMOTE_SERVICE_URL, String.class);
            if ("OK".equalsIgnoreCase(response)) {
                return Health.up().withDetail("RemoteService", "Available").build();
            } else {
                return Health.down().withDetail("RemoteService", "Unavailable").build();
            }
        } catch (Exception e) {
            return Health.down(e).withDetail("RemoteService", "Error").build();
        }
    }
}
