package com.tingco.codechallenge.elevator.service;

import com.tingco.codechallenge.elevator.enums.ElevatorDirection;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * @author Lorinc Sonnevend
 */
public class PassengerElevatorImpl implements Elevator {

    private List<Passenger> passengers;
    private ElevatorDirection direction;
    private TreeSet<Integer> queue;
    private int id;
    private int currentFloor;
    private int minFloor;
    private int maxFloor;

    public PassengerElevatorImpl(int id, @Value("${com.tingco.elevator.minFloor}")int minFloor, @Value("${com.tingco.elevator.maxFloor}") int maxFloor) {
        this.id = id;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.passengers = new ArrayList<>();
        this.queue = new TreeSet<>();
        this.direction = ElevatorDirection.NONE;
        //TODO could set up a different property based on elevator type and initialize the elevator on that floor
        this.currentFloor = minFloor;

    }

    @Override
    public ElevatorDirection getDirection() {
        return direction;
    }

    @Override
    public int getAddressedFloor() {
        //TODO maybe refactor this to Integer, I dont like returning -999 here
        return direction.equals(ElevatorDirection.NONE) ? -999 : queue.first();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void moveElevator(int toFloor) {

        if (outOfLimits(toFloor)) {
            return;
        }

        if (direction.equals(ElevatorDirection.NONE)) {
            if (currentFloor == toFloor) {
                //TODO add passenger}
                return;
            }
            if (currentFloor < toFloor) {
               setDirection(ElevatorDirection.UP);
            } else {
                setDirection(ElevatorDirection.DOWN);
            }
        }
        queue.add(toFloor);
    }

    private boolean outOfLimits(int toFloor) {
        return toFloor > maxFloor || toFloor < minFloor;
    }

    @Override
    public boolean isBusy() {
        return !direction.equals(ElevatorDirection.NONE);
    }

    @Override
    public int currentFloor() {
        return currentFloor;
    }

    public void operate() {
        if (direction.equals(ElevatorDirection.UP)) {
            currentFloor++;
        } else if (direction.equals(ElevatorDirection.DOWN)) {
            currentFloor--;
        }

        if (currentFloor==getAddressedFloor()){
            queue.pollFirst();
            if(queue.isEmpty()){
                setDirection(ElevatorDirection.NONE);
            }
        }
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void loadPassanger(Passenger passanger) {
        this.passengers.add(passanger);
    }

    public void setDirection(ElevatorDirection direction) {
        this.direction = direction;
    }

    public void setId(int id) {
        this.id = id;
    }
}
