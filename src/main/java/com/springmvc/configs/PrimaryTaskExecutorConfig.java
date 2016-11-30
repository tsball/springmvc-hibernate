package com.springmvc.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Fix bug of: Spring Boot with Activiti and websockets
 * That's arguably a bug in Activiti's auto-configuration class. It's relying on their only being a single TaskExecutor bean in the application context or, if there are multiple beans, for one of them to be primary.
 * You should be able to work around the problem by declaring your own TaskExecutor bean and marking it as @Primary:
 *
 */
@Configuration
public class PrimaryTaskExecutorConfig {

    @Primary
    @Bean
    public TaskExecutor primaryTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // Customize executor as appropriate
        return executor;
    }

}
