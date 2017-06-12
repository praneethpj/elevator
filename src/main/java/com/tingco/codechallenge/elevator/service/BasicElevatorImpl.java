package com.tingco.codechallenge.elevator.service;

import com.tingco.codechallenge.elevator.enums.ElevatorDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeSet;

/**
 * @author Lorinc Sonnevend
 */
public class BasicElevatorImpl implements Elevator, Runnable {

    private ElevatorDirection direction;
    private TreeSet<Integer> targetFloors;
    private int id;
    private int currentFloor;
    private int minFloor;
    private int maxFloor;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BasicElevatorImpl(int id, int minFloor, int maxFloor) {
        this.id = id;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.targetFloors = new TreeSet<>();
        this.direction = ElevatorDirection.NONE;
        this.currentFloor = minFloor;
    }

    @Override
    public void run() {
        operate();
    }

    @Override
    public ElevatorDirection getDirection() {
        return direction;
    }

    @Override
    public int getAddressedFloor() {
        //TODO maybe refactor this to Integer, I dont like returning -999 here
        return direction.equals(ElevatorDirection.NONE) ? -999 : direction.equals(ElevatorDirection.UP) ? targetFloors.first() : targetFloors.last();
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
        targetFloors.add(toFloor);
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
        targetFloors.clear();
    }

    @Override
    public void operate() {

        if (direction.equals(ElevatorDirection.UP)) {
            currentFloor++;
            logger.info(this.toString());
        } else if (direction.equals(ElevatorDirection.DOWN)) {
            currentFloor--;
            logger.info(this.toString());
        }

        // if the elevator has no more floors to address, stop it
        if (currentFloor == getAddressedFloor()) {
            if (direction.equals(ElevatorDirection.UP)) {
                targetFloors.pollFirst();
            } else {
                targetFloors.pollLast();
            }
            if (targetFloors.isEmpty()) {
                setDirection(ElevatorDirection.NONE);
                logger.info(this.toString());
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
                ", targetFloors=" + targetFloors +
                ", id=" + id +
                ", currentFloor=" + currentFloor +
                ", minFloor=" + minFloor +
                ", maxFloor=" + maxFloor +
                '}';
    }

}
