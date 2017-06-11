package com.tingco.codechallenge.elevator.service;

import com.tingco.codechallenge.elevator.enums.ElevatorDirection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author Lorinc Sonnevend
 */
@Service
public class BasicElevatorControlSystemImpl implements ElevatorControlSystem {

    private List<Elevator> elevators = new ArrayList<>();
    private Set<Integer> pendingRequests = new TreeSet<>();

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    @Value("${com.tingco.elevator.minFloor}")
    private int minFloor;

    @Value("${com.tingco.elevator.maxFloor}")
    private int maxFloor;

    public BasicElevatorControlSystemImpl() {
    }

    @PostConstruct
    public void setUpElevators(){
        for (int i = 0; i < numberOfElevators; i++) {
            elevators.add(new BasicElevatorImpl(i, minFloor, maxFloor));
        }
    }

    public BasicElevatorControlSystemImpl(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    @Override
    public Elevator requestElevator(int toFloor) {

        Elevator requested = null;
        Optional<Elevator> firstGoingDown = elevators.stream()
                .filter(el -> el.getDirection().equals(ElevatorDirection.DOWN))
                .filter(el -> el.currentFloor() > toFloor)
                .sorted(Comparator.comparingInt(e -> e.currentFloor()))
                .findFirst();
        Optional<Elevator> parkingElevator = elevators.stream().filter(lift -> !lift.isBusy()).findFirst();

        if (parkingElevator.isPresent()) {
            // if there is an elevator going to the ground floor and its current position is above the request
            // pick it up
            // if there is no elevator going down or its bellow the pickup floor, let an idle one do the pickup
            requested = firstGoingDown.orElseGet(parkingElevator::get);
        }

        if (requested != null) {
            requested.moveElevator(toFloor);
        } else {
            pendingRequests.add(toFloor);
        }
        return requested;
    }

    @Override
    public List<Elevator> getElevators() {
        //TODO this list should be turned immutable
        return elevators;
    }

    @Override
    public void releaseElevator(Elevator elevator) {
        if (elevators.contains(elevator)) {
            elevator.reset();
        }
    }

    @Override
    public Set<Integer> getPendingRequests() {
        return pendingRequests;
    }
}
