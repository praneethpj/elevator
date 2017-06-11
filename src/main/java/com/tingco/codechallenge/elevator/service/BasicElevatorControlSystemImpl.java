package com.tingco.codechallenge.elevator.service;

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
                if(elevator != null) {
                    pendingRequests.remove(request);
                }
            });
            elevators.forEach(Elevator::operate);
        } while (true);
    }

    @Override
    public Elevator requestElevator(int toFloor) {
        Optional<Elevator> elevator = elevators.stream().filter(lift -> !lift.isBusy()).findFirst();
        if(!elevator.isPresent()){
            pendingRequests.add(toFloor);
            return null;
        } else {
            return elevator.get();
        }
    }

    @Override
    public List<Elevator> getElevators() {
        //TODO this list should be turned immutable
        return elevators;
    }

    @Override
    public void releaseElevator(Elevator elevator) {
        if(elevators.contains(elevator)){
            elevator.reset();
        }
    }

    @Override
    public Set<Integer> getPendingRequests() {
        return pendingRequests;
    }
}
