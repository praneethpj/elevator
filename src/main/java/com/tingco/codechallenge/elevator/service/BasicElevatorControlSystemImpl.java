package com.tingco.codechallenge.elevator.service;

import com.tingco.codechallenge.elevator.enums.ElevatorDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Lorinc Sonnevend
 */
@Service
public class BasicElevatorControlSystemImpl implements ElevatorControlSystem {

    private List<Elevator> elevators = new ArrayList<>();
    private Queue<Integer> pendingRequests = new LinkedList<>();

    @Autowired
    private ScheduledExecutorService taskExecutor;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    @Value("${com.tingco.elevator.minFloor}")
    private int minFloor;

    @Value("${com.tingco.elevator.maxFloor}")
    private int maxFloor;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BasicElevatorControlSystemImpl() {
    }

    @PreDestroy
    public void tearDown(){
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @PostConstruct
    public void setUpElevators(){
        for (int i = 0; i < numberOfElevators; i++) {
            BasicElevatorImpl basicElevator = new BasicElevatorImpl(i, minFloor, maxFloor);
            elevators.add(basicElevator);
            taskExecutor.scheduleAtFixedRate(basicElevator, 1, 1, TimeUnit.SECONDS);
        }

        executorService.scheduleAtFixedRate(() -> {
            if (!pendingRequests.isEmpty()) {
                Elevator requestElevator = requestElevator(pendingRequests.peek());
                if (requestElevator != null) {
                    pendingRequests.poll();
                    logger.info("removed from queue - "+ pendingRequests);
                }
            }
        }, 1, 2, TimeUnit.SECONDS);

    }

    public BasicElevatorControlSystemImpl(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    @Override
    public Elevator requestElevator(int toFloor) {

        Elevator requested = null;
        Optional<Elevator> firstGoingDown = elevators.stream()
                .filter(el -> el.getDirection().equals(ElevatorDirection.DOWN))
                .filter(el -> el.currentFloor() > toFloor)
                .sorted(Comparator.comparingInt(e -> e.currentFloor()))
                .findFirst();
        Optional<Elevator> parkingElevator = elevators.stream().filter(lift -> !lift.isBusy()).findFirst();

        if (parkingElevator.isPresent()) {
            // if there is an elevator going to the ground floor and its current position is above the request
            // pick it up
            // if there is no elevator going down or its bellow the pickup floor, let an idle one do the pickup
            requested = firstGoingDown.orElseGet(parkingElevator::get);
        }

        if (requested != null) {
            requested.moveElevator(toFloor);
        } else {
            logger.info(">>>added to queue"+toFloor);
            pendingRequests.add(toFloor);
        }
        return requested;
    }

    @Override
    public List<Elevator> getElevators() {
        return elevators;
    }

    @Override
    public void releaseElevator(Elevator elevator) {
        if (elevators.contains(elevator)) {
            elevator.reset();
        }
    }

    @Override
    public Queue<Integer> getPendingRequests() {
        return pendingRequests;
    }
}
