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
        PassengerElevatorImpl passengerElevator = new PassengerElevatorImpl(1, 0, 10);
        PassengerElevatorImpl passengerElevator2 = new PassengerElevatorImpl(1, 0, 10);
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
        PassengerElevatorImpl passengerElevator = new PassengerElevatorImpl(1, 0, 10);
        List<Elevator> elevators = Collections.singletonList(passengerElevator);
        controlSystem = new BasicElevatorControlSystemImpl(elevators);
        //when
        controlSystem.requestElevator(10);
        passengerElevator.operate();
        controlSystem.releaseElevator(passengerElevator);
        //then
        assertEquals("Should not be moving", ElevatorDirection.NONE, passengerElevator.getDirection());
        assertEquals("On wrong floor", 0, passengerElevator.currentFloor());
    }

}