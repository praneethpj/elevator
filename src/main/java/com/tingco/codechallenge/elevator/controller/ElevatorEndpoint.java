package com.tingco.codechallenge.elevator.controller;

import com.tingco.codechallenge.elevator.service.Elevator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Lorinc Sonnevend
 */
public interface ElevatorEndpoint {

    Elevator requestElevator(Integer toFloor);

    List<Elevator> getElevators();

    String ping();
}
