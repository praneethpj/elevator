package com.tingco.codechallenge.elevator.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Lorinc Sonnevend
 */
public interface ElevatorController {
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    String ping();
}
