package com.tingco.codechallenge.elevator.service;

import com.tingco.codechallenge.elevator.enums.ElevatorDirection;
import com.tingco.codechallenge.elevator.enums.ElevatorType;

/**
 * Interface for an elevator object.
 *
 * @author Sven Wesley
 *
 */
public interface Elevator {

    /**
     * Tells which direction is the elevator going in.
     *
     * @return Direction Enumeration value describing the direction.
     */
    ElevatorDirection getDirection();

    /**
     * If the elevator is moving. This is the target floor.
     *
     * @return primitive integer number of floor
     */
    int getAddressedFloor();

    /**
     * Get the Id of this elevator.
     *
     * @return primitive integer representing the elevator.
     */
    int getId();

    /**
     * Command to move the elevator to the given floor.
     *
     * @param toFloor
     *            int where to go.
     */
    void moveElevator(int toFloor);

    /**
     * Check if the elevator is occupied at the moment.
     *
     * @return true if busy.
     */
    boolean isBusy();

    /**
     * Reports which floor the elevator is at right now.
     *
     * @return int actual floor at the moment.
     */
    int currentFloor();

    /**
     * Report the type of the elevator
     *
     * @return Enum with type
     */
    ElevatorType getElevatorType();

    /**
     * Make elevator free for new operations.
     */
    void reset();
}
