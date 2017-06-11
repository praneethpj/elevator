package com.tingco.codechallenge.elevator.service;

import com.tingco.codechallenge.elevator.enums.ElevatorDirection;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Lorinc Sonnevend
 */
public class BasicElevatorControlSystemImplTest {

    ElevatorControlSystem controlSystem;

    @Test
    public void assignRequestToElevator(){
        //given
        BasicElevatorImpl passengerElevator = new BasicElevatorImpl(1, 0, 10);
        BasicElevatorImpl passengerElevator2 = new BasicElevatorImpl(1, 0, 10);
        passengerElevator2.moveElevator(9);
        List<Elevator> elevators = new ArrayList<>();
        elevators.add(passengerElevator);
        elevators.add(passengerElevator2);
        controlSystem = new BasicElevatorControlSystemImpl(elevators);
        //when
        Elevator elevator = controlSystem.requestElevator(10);
        //then
        assertEquals("not the elevator we want", passengerElevator, elevator);
    }

    @Test
    public void returnNullIfNoElevatorIsAvailable(){
        //given
        controlSystem = new BasicElevatorControlSystemImpl(Collections.emptyList());
        //when
        Elevator elevator = controlSystem.requestElevator(10);
        //then
        assertEquals("Elevator should not be available", null, elevator);
        assertFalse("No pending requests", controlSystem.getPendingRequests().isEmpty());
    }

    @Test
    public void releaseElevatorResetsTheElevatorsState(){
        //given
        BasicElevatorImpl passengerElevator = new BasicElevatorImpl(1, 0, 10);
        List<Elevator> elevators = Collections.singletonList(passengerElevator);
        controlSystem = new BasicElevatorControlSystemImpl(elevators);
        //when
        controlSystem.requestElevator(10);
        passengerElevator.operate();
        controlSystem.releaseElevator(passengerElevator);
        //then
        assertEquals("Should not be moving", ElevatorDirection.NONE, passengerElevator.getDirection());
        assertEquals("On wrong floor", 1, passengerElevator.currentFloor());
    }

    @Test
    public void assignRequestToTheElevatorGoingDownIfItsCloser(){
        //given
        BasicElevatorImpl passengerElevator = new BasicElevatorImpl(1, 0, 10);
        BasicElevatorImpl passengerElevator2 = new BasicElevatorImpl(1, 0, 10);
        List<Elevator> elevators = new ArrayList<>();
        elevators.add(passengerElevator);
        elevators.add(passengerElevator2);
        controlSystem = new BasicElevatorControlSystemImpl(elevators);
        //when
        controlSystem.requestElevator(6);
        passengerElevator.operate(); //1
        passengerElevator.operate(); //2
        passengerElevator.operate(); //3
        passengerElevator.operate(); //4
        passengerElevator.operate(); //5
        passengerElevator.operate(); //6
        passengerElevator.operate(); //starts going back to ground floor (5)
        controlSystem.requestElevator(4); // request is closer to an elevator than to the ground floor
        //then
        assertEquals("Should be moving", ElevatorDirection.DOWN, passengerElevator.getDirection());
        assertEquals("To wrong floor", 4, passengerElevator.getAddressedFloor());
        assertEquals("On wrong floor", 5, passengerElevator.currentFloor());
    }

    @Test
    public void stopForOneTurnAfterFulfillingAllRequests(){
        //given
        BasicElevatorImpl passengerElevator = new BasicElevatorImpl(1, 0, 10);
        List<Elevator> elevators = Collections.singletonList(passengerElevator);
        controlSystem = new BasicElevatorControlSystemImpl(elevators);
        //when
        controlSystem.requestElevator(2);
        passengerElevator.operate(); //1
        passengerElevator.operate(); //2
        //then
        assertEquals("Should not be moving", ElevatorDirection.NONE, passengerElevator.getDirection());
        assertEquals("On wrong floor", 2, passengerElevator.currentFloor());
    }

    @Test
    public void idleElevatorNotOnGroundFloorWillPickUpRequestAboveCurrentPosition(){
        //given
        BasicElevatorImpl passengerElevator = new BasicElevatorImpl(1, 0, 10);
        List<Elevator> elevators = Collections.singletonList(passengerElevator);
        controlSystem = new BasicElevatorControlSystemImpl(elevators);
        //when
        controlSystem.requestElevator(2);
        passengerElevator.operate(); //1
        passengerElevator.operate(); //2
        controlSystem.requestElevator(3);
        //then
        assertEquals("Should be moving", ElevatorDirection.UP, passengerElevator.getDirection());
        assertEquals("On wrong floor", 2, passengerElevator.currentFloor());
    }

}