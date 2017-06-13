package com.tingco.codechallenge.elevator.service;

import com.google.common.eventbus.EventBus;
import com.tingco.codechallenge.elevator.enums.ElevatorDirection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tingco.codechallenge.elevator.service.ElevatorEvent.EventType.ELEVATOR_ASSIGNED;
import static com.tingco.codechallenge.elevator.service.ElevatorEvent.EventType.ELEVATOR_RESETED;
import static com.tingco.codechallenge.elevator.service.ElevatorEvent.EventType.PENDING_REQUEST;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Lorinc Sonnevend
 */
@RunWith(MockitoJUnitRunner.class)
public class BasicElevatorControlSystemImplTest {

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private BasicElevatorControlSystemImpl controlSystem;

    @Test
    public void assignRequestToElevator(){
        //given
        BasicElevatorImpl passengerElevator = new BasicElevatorImpl(0, 0, 10);
        BasicElevatorImpl passengerElevator2 = new BasicElevatorImpl(1, 0, 10);
        passengerElevator2.moveElevator(9);
        List<Elevator> elevators = new ArrayList<>();
        elevators.add(passengerElevator);
        elevators.add(passengerElevator2);
        controlSystem.setElevators(elevators);
        //when
        Elevator elevator = controlSystem.requestElevator(10);
        //then
        assertEquals("not the elevator we want", passengerElevator, elevator);
    }

    @Test
    public void returnNullIfNoElevatorIsAvailable(){
        //given
        controlSystem.setElevators(Collections.emptyList());
        //when
        Elevator elevator = controlSystem.requestElevator(10);
        //then
        assertEquals("Elevator should not be available", null, elevator);
        assertFalse("No pending requests", controlSystem.getPendingRequests().isEmpty());
        verify(eventBus, times(1)).post(new ElevatorEventBuilder()
                .setEventType(PENDING_REQUEST)
                .createElevatorEvent());
    }

    @Test
    public void releaseElevatorResetsTheElevatorsState(){
        //given
        BasicElevatorImpl passengerElevator = new BasicElevatorImpl(1, 0, 10);
        List<Elevator> elevators = Collections.singletonList(passengerElevator);
        controlSystem.setElevators(elevators);
        //when
        controlSystem.requestElevator(10);
        passengerElevator.operate();
        controlSystem.releaseElevator(passengerElevator);
        //then
        assertEquals("Should not be moving", ElevatorDirection.NONE, passengerElevator.getDirection());
        assertEquals("On wrong floor", 1, passengerElevator.currentFloor());
        verify(eventBus, times(1)).post(new ElevatorEventBuilder()
                .setEventType(ELEVATOR_RESETED)
                .setElevatorId(1)
                .createElevatorEvent());
    }

    @Test
    public void assignRequestToIdleElevator(){
        //given
        BasicElevatorImpl passengerElevator = new BasicElevatorImpl(0, 0, 10);
        BasicElevatorImpl passengerElevator2 = new BasicElevatorImpl(1, 0, 10);
        List<Elevator> elevators = new ArrayList<>();
        elevators.add(passengerElevator);
        elevators.add(passengerElevator2);
        controlSystem.setElevators(elevators);
        //when
        controlSystem.requestElevator(6);
        passengerElevator.operate(); //1
        controlSystem.requestElevator(4); // request is closer to an elevator than to the ground floor
        //then

        assertEquals("Should be moving", ElevatorDirection.UP, passengerElevator.getDirection());
        assertEquals("To wrong floor", 6, passengerElevator.getAddressedFloor());
        assertEquals("On wrong floor", 4, passengerElevator2.getAddressedFloor());
        verify(eventBus, times(1)).post(new ElevatorEventBuilder()
                .setEventType(ELEVATOR_ASSIGNED)
                .setElevatorId(0)
                .createElevatorEvent());
        verify(eventBus, times(1)).post(new ElevatorEventBuilder()
                .setEventType(ELEVATOR_ASSIGNED)
                .setElevatorId(1)
                .createElevatorEvent());
    }

    @Test
    public void assignElevatorCloserToTarget(){
        //given
        BasicElevatorImpl passengerElevator = new BasicElevatorImpl(0, 0, 10);
        BasicElevatorImpl passengerElevator2 = new BasicElevatorImpl(1, 5, 10);
        List<Elevator> elevators = new ArrayList<>();
        elevators.add(passengerElevator);
        elevators.add(passengerElevator2);
        controlSystem.setElevators(elevators);
        //when
        controlSystem.requestElevator(6);
        //then

        assertEquals("Should be moving", ElevatorDirection.UP, passengerElevator2.getDirection());
        assertEquals("Should not be moving", ElevatorDirection.NONE, passengerElevator.getDirection());
        assertEquals("To wrong floor", 0, passengerElevator.getAddressedFloor());
        assertEquals("On wrong floor", 6, passengerElevator2.getAddressedFloor());
        verify(eventBus, times(1)).post(new ElevatorEventBuilder()
                .setEventType(ELEVATOR_ASSIGNED)
                .setElevatorId(1)
                .createElevatorEvent());
    }

    @Test
    public void stopForOneTurnAfterFulfillingAllRequests(){
        //given
        BasicElevatorImpl passengerElevator = new BasicElevatorImpl(1, 0, 10);
        List<Elevator> elevators = Collections.singletonList(passengerElevator);
        controlSystem.setElevators(elevators);
        //when
        controlSystem.requestElevator(2);
        passengerElevator.operate(); //1
        passengerElevator.operate(); //2
        //then
        verify(eventBus, times(1)).post(new ElevatorEventBuilder()
                .setEventType(ELEVATOR_ASSIGNED)
                .setElevatorId(1)
                .createElevatorEvent());
        assertEquals("Should not be moving", ElevatorDirection.NONE, passengerElevator.getDirection());
        assertEquals("On wrong floor", 2, passengerElevator.currentFloor());
    }

}