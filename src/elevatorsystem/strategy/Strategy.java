package elevatorsystem.strategy;

import elevatorsystem.elevator.Elevator;
import elevatorsystem.event.Event;
import elevatorsystem.io.BST;
import elevatorsystem.io.ButtonSensor;
import elevatorsystem.requests.CallRequest;
import elevatorsystem.requests.Request;
import elevatorsystem.requests.TargetRequest;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class was created by timwedde on 19.11.15.
 */
public abstract class Strategy {

    protected BlockingQueue<TargetRequest> targetRequests = new LinkedBlockingQueue<>();
    protected ArrayList<CallRequest> callRequests = new ArrayList<>();
    protected ArrayList<Elevator> elevatorList = new ArrayList<>();
    
    /**
     * Adds a new target request to the strategy.
     * Does not add duplicate requests.
     *
     * @param r The new request
     */
    public void addTargetRequest(TargetRequest r){
        // Step 1: Check for existing requests for the same floor AND the same elevator
        for(Request req : targetRequests){
            if(req.getShaftNr() == r.getShaftNr() && req.getDestination() == r.getDestination()){
                return;
            }
        }
        // Step 2: Add request
        targetRequests.add(r);
        triggerEvent(new Event(new ButtonSensor(8, BST.TARGETBUTTON, r.getDestination()), r.getShaftNr()));
    }

    /**
     * Adds a new call request to the strategy.
     * Does not add duplicate requests.
     *
     * @param r The new request
     */
    public void addCallRequest(CallRequest r){
        // Step 1: Check for existing requests for same floor
        for(Request req : callRequests){ if(req.getDestination() == r.getDestination()) return; }
        // Step 2: Add request
        callRequests.add(r);
    }

    /**
     * Is called whenever an event is forwarded to the server.
     *
     * @param e The event
     */
    public abstract void triggerEvent(Event e);
    
    /**
     * Allows for calling methods on elevators
     *
     * @param elevators The list of elevators
     */
    public void setElevators(ArrayList<Elevator> elevators){
        this.elevatorList = elevators;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
    /**
     * Sets the behaviour of the strategy
     *
     */
}