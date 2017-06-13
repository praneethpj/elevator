package com.tingco.codechallenge.elevator.controller;

import com.tingco.codechallenge.elevator.service.Elevator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Lorinc Sonnevend
 */
public interface ElevatorEndpoint {

    /**
     * Request {@link Elevator} to floor
     *
     * @return Requested elevator or null
     */
    ResponseEntity<Elevator> requestElevator(Integer toFloor);

    /**
     * Report list of  {@link Elevator}
     *
     * @return list of elevators
     */
    List<Elevator> getElevators();

    /**
     * Release {@link Elevator}
     *
     * @return whether release was successful
     */
    ResponseEntity<Boolean> releaseElevator(Integer elevatorId);

    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    String ping();
}
