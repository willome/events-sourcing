package com.github.willome.events.sourcing.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Spring Boot Configuration
 *
 * @author willome
 *
 */
@Configuration
@ComponentScan("com.github.willome.events.sourcing")
@EnableJpaRepositories({ "com.github.willome.events.sourcing.repository" })
@EntityScan({ "com.github.willome.events.sourcing.domain" })
public class EventsSourcingConfiguration {

    /**
     * Deactivate Schedulers for tests purpose.
     *
     * @author Axione
     *
     */
    @ConditionalOnProperty(value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true)
    @Configuration
    @EnableScheduling
    public static class SchedulingConfiguration {

        /**
         * Provide a thread pool used for each Shedule task.
         *
         * @return ThreadPoolTaskScheduler
         */
        @Bean
        public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
            ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
            threadPoolTaskScheduler.setPoolSize(5);
            return threadPoolTaskScheduler;
        }

    }

    /**
     * Deactive Async for tests purpose
     *
     * @author Axione
     *
     */
    @ConditionalOnProperty(value = "app.async.enable", havingValue = "true", matchIfMissing = true)
    @Configuration
    @EnableAsync(mode = AdviceMode.ASPECTJ)
    public static class AsyncConfiguration {

        /**
         *
         * @return TaskExecutor
         */
        @Bean(name = "eventsTaskExecutor")
        public TaskExecutor threadPoolTaskExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(4);
            executor.setMaxPoolSize(5);
            executor.setThreadNamePrefix("events");
            executor.initialize();
            return executor;
        }

    }

}
