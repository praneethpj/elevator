package com.tingco.codechallenge.elevator.service;

import com.tingco.codechallenge.elevator.enums.ElevatorDirection;

import java.util.*;

/**
 * @author Lorinc Sonnevend
 */
public class BasicElevatorControlSystemImpl implements ElevatorControlSystem {

    private List<Elevator> elevators = new ArrayList<>();
    private Set<Integer> pendingRequests = new TreeSet<>();

    public BasicElevatorControlSystemImpl(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public void start() throws InterruptedException {
        do {
            Thread.sleep(1000);
            pendingRequests.forEach(request -> {
                Elevator elevator = requestElevator(request);
                if (elevator != null) {
                    pendingRequests.remove(request);
                }
            });
            elevators.forEach(Elevator::operate);
        } while (true);
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
            // if there is no elevator going down or its bellow the pickup floor, let and idle one do the pickup
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
