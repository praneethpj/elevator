package com.tingco.codechallenge.elevator.controller;

import com.tingco.codechallenge.elevator.service.Elevator;
import com.tingco.codechallenge.elevator.service.ElevatorControlSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    @Value("${com.tingco.elevator.minFloor}")
    private int minFloor;

    @Value("${com.tingco.elevator.maxFloor}")
    private int maxFloor;

    private final ElevatorControlSystem elevatorControlSystem;

    @Autowired
    public ElevatorEndpointImpl(ElevatorControlSystem elevatorControlSystem) {
        this.elevatorControlSystem = elevatorControlSystem;
    }

    @Override
    @RequestMapping(value = "/request/{toFloor}", method = RequestMethod.POST)
    public ResponseEntity<Elevator> requestElevator(@PathVariable Integer toFloor) {
        if (toFloor < minFloor || toFloor > maxFloor) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(elevatorControlSystem.requestElevator(toFloor));
    }

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Elevator> getElevators() {
        return elevatorControlSystem.getElevators();
    }

    @Override
    @RequestMapping(value = "/release/{elevatorId}", method = RequestMethod.POST)
    public ResponseEntity<Boolean> releaseElevator(@PathVariable Integer elevatorId) {
        if (elevatorId < 0 || elevatorId >= numberOfElevators) {
            return ResponseEntity.notFound().build();
        }
        elevatorControlSystem.releaseElevator(elevatorControlSystem.getElevators().get(elevatorId));
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @Override
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        return "pong";
    }
}
