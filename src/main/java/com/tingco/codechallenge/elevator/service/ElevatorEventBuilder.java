package com.tingco.codechallenge.elevator.service;

public class ElevatorEventBuilder {
    private int elevatorId;
    private ElevatorEvent.EventType eventType;

    public ElevatorEventBuilder setElevatorId(int elevatorId) {
        this.elevatorId = elevatorId;
        return this;
    }

    public ElevatorEventBuilder setEventType(ElevatorEvent.EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public ElevatorEvent createElevatorEvent() {
        return new ElevatorEvent(elevatorId, eventType);
    }
}