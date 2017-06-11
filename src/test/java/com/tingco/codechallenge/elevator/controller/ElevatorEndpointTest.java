package com.tingco.codechallenge.elevator.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tingco.codechallenge.elevator.ElevatorApplication;

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ElevatorApplication.class)
public class ElevatorEndpointTest {

    @Autowired
    private ElevatorEndpoint endpoint;

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    @Test
    public void ping() {

        Assert.assertEquals("pong", endpoint.ping());

    }

    @Test
    public void requestElevator() {

        Assert.assertEquals(0, endpoint.requestElevator(1).getId());

    }
    @Test
    public void getElevators() {

        Assert.assertEquals(numberOfElevators, endpoint.getElevators().size());

    }

}
