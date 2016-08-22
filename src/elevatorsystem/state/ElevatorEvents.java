package elevatorsystem.state;

import elevatorsystem.elevator.Elevator;

/**
 * This class was created by timwedde on 19.11.15.
 */
public interface ElevatorEvents {

    void entry(Elevator context);

    void exit(Elevator context);

    void idleTimeout(Elevator context);

    void obstruct(Elevator context);

    void doorClose(Elevator context);

    void doorOpen(Elevator context);
}