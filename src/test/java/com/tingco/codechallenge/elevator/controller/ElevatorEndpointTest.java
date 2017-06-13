package com.tingco.codechallenge.elevator.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tingco.codechallenge.elevator.ElevatorApplication;

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
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
        Assert.assertEquals(1, endpoint.requestElevator(1).getBody().getAddressedFloor());
        Assert.assertEquals(HttpStatus.OK, endpoint.requestElevator(1).getStatusCode());
    }

    @Test
    public void requestElevatorWithWrongFloor() {
        Assert.assertEquals(HttpStatus.BAD_REQUEST, endpoint.requestElevator(111).getStatusCode());
    }

    @Test
    public void getElevators() {
        Assert.assertEquals(numberOfElevators, endpoint.getElevators().size());
    }


    @Test
    public void releaseElevator() {
        Assert.assertEquals(ResponseEntity.ok(Boolean.TRUE), endpoint.releaseElevator(0));
    }

    @Test
    public void releaseElevatorWithWrongId() {
        Assert.assertEquals(ResponseEntity.notFound().build(), endpoint.releaseElevator(numberOfElevators + 99));
    }

}
