package com.epam.labaratory.springboottask.indicator.health;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DatabaseHealthIndicator implements HealthIndicator {

    DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1000)) {
                return Health.up().withDetail("Database", "Available").build();
            } else {
                return Health.down().withDetail("Database", "Unavailable").build();
            }
        } catch (SQLException e) {
            return Health.down(e).withDetail("Database", "Error").build();
        }
    }
}