package com.tingco.codechallenge.elevator.service;

import com.tingco.codechallenge.elevator.enums.ElevatorDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lorinc Sonnevend
 */
public class BasicElevatorImpl implements Elevator, Runnable {

    private ElevatorDirection direction;
    private int id;
    private int currentFloor;
    private int minFloor;
    private int maxFloor;
    private int addressedFloor;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BasicElevatorImpl(int id, int minFloor, int maxFloor) {
        this.id = id;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.direction = ElevatorDirection.NONE;
        this.currentFloor = minFloor;
        this.addressedFloor = minFloor;
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
        return addressedFloor;
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

        if(currentFloor==toFloor){
            return;
        }

        if (currentFloor < toFloor) {
            setDirection(ElevatorDirection.UP);
        } else {
            setDirection(ElevatorDirection.DOWN);
        }

        addressedFloor = toFloor;
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
        addressedFloor = currentFloor;
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
        if (!direction.equals(ElevatorDirection.NONE) && currentFloor == addressedFloor) {
            setDirection(ElevatorDirection.NONE);
            logger.info(this.toString());
        }
    }

    public void setDirection(ElevatorDirection direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "BasicElevatorImpl{" +
                "direction=" + direction +
                ", id=" + id +
                ", currentFloor=" + currentFloor +
                ", minFloor=" + minFloor +
                ", maxFloor=" + maxFloor +
                ", addressedFloor=" + addressedFloor +
                '}';
    }
}
