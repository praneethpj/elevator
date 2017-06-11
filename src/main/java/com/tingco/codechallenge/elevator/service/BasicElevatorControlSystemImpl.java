package com.tingco.codechallenge.elevator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Lorinc Sonnevend
 */
public class BasicElevatorControlSystemImpl implements ElevatorControlSystem {

    private List<Elevator> elevators = new ArrayList<>();

    public BasicElevatorControlSystemImpl(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    @Override
    public Elevator requestElevator(int toFloor) {
        Optional<Elevator> elevator = elevators.stream().filter(lift -> !lift.isBusy()).findFirst();
        return elevator.orElse(null);
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
}
