package com.tingco.codechallenge;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Preconfigured Spring Application boot class.
 */
@Configuration
@ComponentScan(basePackages = {"com.tingco.codechallenge.elevator"})
@EnableAutoConfiguration
@PropertySources({@PropertySource("classpath:application.properties")})
public class ElevatorApplication {

    /**
     * Start method that will be invoked when starting the Spring context.
     *
     * @param args Not in use
     */
    public static void main(final String[] args) {
        SpringApplication.run(ElevatorApplication.class, args);
    }

}
