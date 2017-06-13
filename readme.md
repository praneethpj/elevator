# Elevator Coding Challenge

## How to start
build and run the code with Maven

    mvn package
    mvn spring-boot:run

or start the target JAR file 

    mvn package
    java -jar target/elevator-1.0-SNAPSHOT.jar

## How to access endpoints

### Request an elevator

    POST request to http://localhost:8080/elevator/rest/v1/request/{toFloor}

### Get list of elevators

    GET request to http://localhost:8080/elevator/rest/v1/list

### Release and elevator

    POST request to http://localhost:8080/elevator/rest/v1/release/{elevatorId}

## Implementation notes

I did a small refactoring of the skeleton code to fit my coding style. Hope that's not
causing too much trouble :)

I added Aspect based logging on the service package to make the application more
production like. I added the Spring AOP package to the pom dependencies.

I used the provided Guava EventBus bean to post application tracking events
for future use.

The provided scheduled pool bean is used to run the elevators.
An additional one is created in the BasicElevatorControlSystem to manage the pending
requests.

I built the application using TDD process and tried to cover all the corner cases
while developing the base classes. The total test coverage is above 90 percent.

The IntegrationTest class holds a basic simulation, I used the provided Awaitability
package for that. 

## TODO

Better exception/error handling
