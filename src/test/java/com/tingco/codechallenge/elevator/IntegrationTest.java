package com.tingco.codechallenge.elevator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tingco.codechallenge.ElevatorApplication;

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ElevatorApplication.class)
public class IntegrationTest {


    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    @Test
    public void simulateAnElevatorShaft() {

    }

}
