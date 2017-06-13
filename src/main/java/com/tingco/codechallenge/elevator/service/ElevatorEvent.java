package com.tingco.codechallenge.elevator.service;

import com.tingco.codechallenge.elevator.enums.ElevatorDirection;

/**
 * @author Lorinc Sonnevend
 */
public class ElevatorEvent {

    private int elevatorId;

    private EventType eventType;

    enum EventType {
        ELEVATOR_ASSIGNED,
        PENDING_REQUEST,
        MOVING_UP,
        MOVING_DOWN,
        BECAME_IDLE,
        ELEVATOR_RESETED
    }

    public ElevatorEvent(int elevatorId, EventType eventType) {
        this.elevatorId = elevatorId;
        this.eventType = eventType;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(int elevatorId) {
        this.elevatorId = elevatorId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElevatorEvent that = (ElevatorEvent) o;

        if (elevatorId != that.elevatorId) return false;
        return eventType == that.eventType;
    }

    @Override
    public int hashCode() {
        int result = elevatorId;
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ElevatorEvent{" +
                "elevatorId=" + elevatorId +
                ", eventType=" + eventType +
                '}';
    }
}
