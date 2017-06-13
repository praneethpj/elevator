package com.tingco.codechallenge.elevator;

import com.tingco.codechallenge.elevator.controller.ElevatorEndpoint;
import com.tingco.codechallenge.elevator.enums.ElevatorDirection;
import com.tingco.codechallenge.elevator.service.Elevator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ElevatorApplication.class)
public class IntegrationTest {

    @Autowired
    private ElevatorEndpoint endpoint;

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    @Test
    public void simulateAnElevatorShaft() {
        // should be assigned to the first elevator
        endpoint.requestElevator(10);
        endpoint.requestElevator(10);
        endpoint.requestElevator(10);
        endpoint.requestElevator(10);
        endpoint.requestElevator(10);
        await().atMost(2, SECONDS).until(() -> endpoint.getElevators().get(0).isBusy());
        // should be moving up
        await().atMost(1, SECONDS).until(() -> endpoint.getElevators().get(0).getDirection().equals(ElevatorDirection.UP));
        endpoint.requestElevator(5);
        // should be assigned to last elevator
        await().atMost(5, SECONDS).until(() -> endpoint.getElevators().get(5).currentFloor() == 4);
        // release an elevator
        endpoint.releaseElevator(1);
        await().atMost(1, SECONDS).until(() -> !endpoint.getElevators().get(1).isBusy());
        endpoint.requestElevator(10);
        // add pending request
        endpoint.requestElevator(0);
        // elevator 5 picks up the pending request as its the closer to targer floor
        await().atMost(4, SECONDS).until(() -> {
            Elevator elevator = endpoint.getElevators().get(5);
            return elevator.isBusy() && elevator.getDirection().equals(ElevatorDirection.DOWN);
        });
        // other elevators should have arrived to floor 10
        await().atMost(11, SECONDS).until(() -> {
            Elevator elevator = endpoint.getElevators().get(0);
            return !elevator.isBusy() && elevator.getDirection().equals(ElevatorDirection.NONE) && elevator.currentFloor() == (10);
        });
        await().atMost(11, SECONDS).until(() -> {
            Elevator elevator = endpoint.getElevators().get(1);
            return !elevator.isBusy() && elevator.getDirection().equals(ElevatorDirection.NONE) && elevator.currentFloor() == (10);
        });
        await().atMost(11, SECONDS).until(() -> {
            Elevator elevator = endpoint.getElevators().get(2);
            return !elevator.isBusy() && elevator.getDirection().equals(ElevatorDirection.NONE) && elevator.currentFloor() == (10);
        });
        await().atMost(11, SECONDS).until(() -> {
            Elevator elevator = endpoint.getElevators().get(3);
            return !elevator.isBusy() && elevator.getDirection().equals(ElevatorDirection.NONE) && elevator.currentFloor() == (10);
        });
        await().atMost(11, SECONDS).until(() -> {
            Elevator elevator = endpoint.getElevators().get(4);
            return !elevator.isBusy() && elevator.getDirection().equals(ElevatorDirection.NONE) && elevator.currentFloor() == (10);
        });


    }

}
