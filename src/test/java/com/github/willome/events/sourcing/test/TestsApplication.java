package com.github.willome.events.sourcing.test;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.github.willome.events.sourcing.publisher.EventPublisher;

@SpringBootApplication
@ComponentScan({ "com.github.willome.events.sourcing" })
@EnableJpaRepositories({ "com.github.willome.events.sourcing.test" })
@EntityScan({ "com.github.willome.events.sourcing.test" })
@Configuration
public class TestsApplication {

    /**
     *
     * @return EventPublisher
     */
    @Bean
    public EventPublisher eventPublisher() {
        return Mockito.mock(EventPublisher.class);
    }

}
