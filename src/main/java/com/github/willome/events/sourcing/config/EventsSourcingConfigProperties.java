package com.github.willome.events.sourcing.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author Axione
 *
 */
@Component
@ConfigurationProperties(prefix = "events")
public class EventsSourcingConfigProperties {

    /**
     *
     * @author Axione
     *
     */
    public static class Publisher {

        private int frequence = 3000;
        private Map<String, String> queues = new HashMap<>();

        public Map<String, String> getQueues() {
            return queues;
        }

        public void setQueues(Map<String, String> queues) {
            this.queues = queues;
        }

        public int getFrequence() {
            return frequence;
        }

        public void setFrequence(int frequence) {
            this.frequence = frequence;
        }

    }

    private final Publisher publisher = new Publisher();

    public Publisher getPublisher() {
        return publisher;
    }

}
