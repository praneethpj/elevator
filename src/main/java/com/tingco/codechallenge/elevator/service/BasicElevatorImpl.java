package com.tingco.codechallenge.elevator.service;

import com.tingco.codechallenge.elevator.enums.ElevatorDirection;

import java.util.TreeSet;

/**
 * @author Lorinc Sonnevend
 */
public class BasicElevatorImpl implements Elevator {

    private ElevatorDirection direction;
    private TreeSet<Integer> queue;
    private int id;
    private int currentFloor;
    private int minFloor;
    private int maxFloor;


    public BasicElevatorImpl(int id, int minFloor, int maxFloor) {
        this.id = id;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.queue = new TreeSet<>();
        this.direction = ElevatorDirection.NONE;
        this.currentFloor = minFloor;

    }

    @Override
    public ElevatorDirection getDirection() {
        return direction;
    }

    @Override
    public int getAddressedFloor() {
        //TODO maybe refactor this to Integer, I dont like returning -999 here
        return direction.equals(ElevatorDirection.NONE) ? -999 : direction.equals(ElevatorDirection.UP) ? queue.first() : queue.last();
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

    @Override
    public void reset() {
        setDirection(ElevatorDirection.NONE);
        queue.clear();
    }

    @Override
    public void operate() {
        // if the elevator stopped on a floor other than the ground floor
        // start moving it down to ground floor
        if (direction.equals(ElevatorDirection.NONE) && currentFloor != minFloor) {
            setDirection(ElevatorDirection.DOWN);
            queue.add(minFloor);
        }

        if (direction.equals(ElevatorDirection.UP)) {
            currentFloor++;
        } else if (direction.equals(ElevatorDirection.DOWN)) {
            currentFloor--;
        }

        // if the elevator has no more floors to address, stop it
        if (currentFloor == getAddressedFloor()) {
            if (direction.equals(ElevatorDirection.UP)) {
                queue.pollFirst();
            } else {
                queue.pollLast();
            }
            if (queue.isEmpty()) {
                setDirection(ElevatorDirection.NONE);
            }
        }
    }

    public void setDirection(ElevatorDirection direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "BasicElevatorImpl{" +
                "direction=" + direction +
                ", queue=" + queue +
                ", id=" + id +
                ", currentFloor=" + currentFloor +
                ", minFloor=" + minFloor +
                ", maxFloor=" + maxFloor +
                '}';
    }
}
