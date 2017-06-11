package com.tingco.codechallenge.elevator.service;

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
        assertEquals("not the elevator we want", null, elevator);
    }

}