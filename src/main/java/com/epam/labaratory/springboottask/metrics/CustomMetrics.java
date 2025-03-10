package com.epam.labaratory.springboottask.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final Counter customMetricCounter;

    public CustomMetrics(MeterRegistry registry) {
        this.customMetricCounter = registry.counter("custom_metric_counter");
    }

    public void incrementCustomMetric() {
        customMetricCounter.increment();
    }
}