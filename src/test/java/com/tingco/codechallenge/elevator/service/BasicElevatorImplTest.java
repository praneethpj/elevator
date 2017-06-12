package com.tingco.codechallenge.elevator.service;

import com.tingco.codechallenge.elevator.enums.ElevatorDirection;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Lorinc Sonnevend
 */
public class BasicElevatorImplTest {

    private int minFloor = 0;
    private int maxFloor = 10;

    //given
    private BasicElevatorImpl elevator = new BasicElevatorImpl(1, minFloor, maxFloor);

    @Test
    public void directionIsNoneWhenInitialized() {
        assertEquals("direction in wrong state", ElevatorDirection.NONE, elevator.getDirection());
    }

    @Test
    public void elevatorIsOnMinimumFloorWhenInitialized() {
        assertEquals("minFloor is not initialized correctly", minFloor, elevator.currentFloor());
    }

    @Test
    public void elevatorIsNotBusyWhenInitialized() {
        assertEquals("should not be busy", false, elevator.isBusy());
    }

    @Test
    public void moveElevatorToAFloorAboveCurrentPosition() {
        //when
        elevator.moveElevator(maxFloor);
        //then
        assertEquals("Not moving up", ElevatorDirection.UP, elevator.getDirection());
        assertEquals("Wrong addressed floor", maxFloor, elevator.getAddressedFloor());
        assertEquals("Not Busy", true, elevator.isBusy());
    }

    @Test
    public void doNotMoveElevatorIfOnTheRequestedFloor() {
        //when
        elevator.moveElevator(minFloor);
        //then
        assertEquals("Is moving", ElevatorDirection.NONE, elevator.getDirection());
        assertEquals("Wrong addressed floor", minFloor, elevator.getAddressedFloor());
        assertEquals("Busy", false, elevator.isBusy());
    }

    @Test
    public void cannotMoveAboveLimits() {
        //when
        elevator.moveElevator(maxFloor + 1);
        //then
        assertEquals("Is moving", ElevatorDirection.NONE, elevator.getDirection());
        assertEquals("Wrong addressed floor", minFloor, elevator.getAddressedFloor());
        assertEquals("Busy", false, elevator.isBusy());
    }

    @Test
    public void cannotMoveBellowLimits() {
        //when
        elevator.moveElevator(minFloor - 1);
        //then
        assertEquals("Is moving", ElevatorDirection.NONE, elevator.getDirection());
        assertEquals("Wrong addressed floor", minFloor, elevator.getAddressedFloor());
        assertEquals("Wrong floor", minFloor, elevator.currentFloor());
        assertEquals("Busy", false, elevator.isBusy());
    }

    @Test
    public void shouldMoveOneFloorInTheDirectionOfTargetFloor() {
        //when
        elevator.moveElevator(maxFloor);
        elevator.operate();
        //then
        assertEquals("Not moving in the right direction", ElevatorDirection.UP, elevator.getDirection());
        assertEquals("Wrong addressed floor", maxFloor, elevator.getAddressedFloor());
        assertEquals("On wrong floor", minFloor + 1, elevator.currentFloor());
        assertEquals("Not busy", true, elevator.isBusy());
    }

    @Test
    public void shouldStopAtDestination() {
        //when
        elevator.moveElevator(minFloor + 1);
        elevator.operate();
        //then
        assertEquals("Is moving", ElevatorDirection.NONE, elevator.getDirection());
        assertEquals("Wrong addressed floor", minFloor + 1, elevator.getAddressedFloor());
        assertEquals("On wrong floor", minFloor + 1, elevator.currentFloor());
        assertEquals("Is busy", false, elevator.isBusy());
    }

    @Test
    public void shouldStopAtDestinationButContinueIfHasMoreInTheQueue() {
        //when
        elevator.moveElevator(minFloor + 1);
        elevator.moveElevator(minFloor + 2);
        elevator.operate();
        //then
        assertEquals("Not moving in the right direction", ElevatorDirection.UP, elevator.getDirection());
        assertEquals("Wrong addressed floor", minFloor + 2, elevator.getAddressedFloor());
        assertEquals("On wrong floor", minFloor + 1, elevator.currentFloor());
        assertEquals("Not busy", true, elevator.isBusy());
    }

    @Test
    public void canChangeDirectionIfRequestsHaveBeenServed() {
        //when
        elevator.moveElevator(minFloor + 1);
        elevator.operate();
        elevator.moveElevator(minFloor);
        //then
        assertEquals("Not moving in the right direction", ElevatorDirection.DOWN, elevator.getDirection());
        assertEquals("Wrong addressed floor", minFloor, elevator.getAddressedFloor());
        assertEquals("On wrong floor", minFloor + 1, elevator.currentFloor());
        assertEquals("Not busy", true, elevator.isBusy());
    }

    @Test
    public void canMoveDown() {
        //when
        elevator.moveElevator(minFloor + 1);
        elevator.operate();
        elevator.moveElevator(minFloor);
        elevator.operate();
        //then
        assertEquals("Moving", ElevatorDirection.NONE, elevator.getDirection());
        assertEquals("Not on current floor", minFloor, elevator.getAddressedFloor());
        assertEquals("On wrong floor", minFloor, elevator.currentFloor());
        assertEquals("Busy", false, elevator.isBusy());
    }

    @Test
    public void stopIfHasNoMoreRequests() {
        //when
        elevator.moveElevator(minFloor + 2);
        elevator.operate(); //min+1
        elevator.operate(); //min+2
        elevator.operate();
        elevator.operate();
        elevator.operate();
        //then
        assertEquals("Moving", ElevatorDirection.NONE, elevator.getDirection());
        assertEquals("Empty queue", minFloor + 2, elevator.getAddressedFloor());
        assertEquals("On wrong floor", minFloor + 2, elevator.currentFloor());
        assertFalse("Busy", elevator.isBusy());
    }


    @Test
    public void shouldResetTheElevator() {
        //when
        elevator.moveElevator(minFloor + 1);
        elevator.moveElevator(minFloor + 2);
        elevator.operate();
        elevator.reset();
        //then
        assertEquals("Moving", ElevatorDirection.NONE, elevator.getDirection());
        assertEquals("Not current floor", 1, elevator.getAddressedFloor());
        assertEquals("On wrong floor", minFloor + 1, elevator.currentFloor());
        assertEquals("Busy", false, elevator.isBusy());
    }
}