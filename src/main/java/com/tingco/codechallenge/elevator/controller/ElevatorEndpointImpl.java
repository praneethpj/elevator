package com.tingco.codechallenge.elevator.controller;

import com.tingco.codechallenge.elevator.service.Elevator;
import com.tingco.codechallenge.elevator.service.ElevatorControlSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest Resource.
 *
 * @author Sven Wesley
 */
@RestController
@RequestMapping("/rest/v1")
public final class ElevatorEndpointImpl implements ElevatorEndpoint {

    private final ElevatorControlSystem elevatorControlSystem;

    @Autowired
    public ElevatorEndpointImpl(ElevatorControlSystem elevatorControlSystem) {
        this.elevatorControlSystem = elevatorControlSystem;
    }

    /**
     * Request {@link Elevator} to floor
     *
     * @return Requested elevator or null
     */
    @Override
    @RequestMapping(value = "/request/{toFloor}", method = RequestMethod.POST)
    public Elevator requestElevator(@PathVariable Integer toFloor) {
        return elevatorControlSystem.requestElevator(toFloor);
    }

    /**
     * Report list of  {@link Elevator}
     *
     * @return list of elevators
     */
    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Elevator> getElevators() {
        return elevatorControlSystem.getElevators();
    }

    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    @Override
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        return "pong";
    }
}
