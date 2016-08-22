package elevatorsystem.strategy;

import elevatorsystem.elevator.Elevator;
import elevatorsystem.event.Event;
import elevatorsystem.io.BST;
import elevatorsystem.io.ButtonSensor;
import elevatorsystem.requests.CallRequest;
import elevatorsystem.requests.Request;
import elevatorsystem.requests.TargetRequest;
import elevatorsystem.state.State;
import elevatorsystem.util.Direction;
import java.util.ArrayList;

/**
 * Full Pater Noster: Always make a complete circular movement between lowest and highest floor. 
 * That is:  reverse direction of the elevator only at the top or bottom floor. 
 * The movement  stops  if  there  are  no  more  requests.
 * On arrival of request the movement is resumed in the same direction.
 * 
 * @author Bas
 */
public class FPNStrategy2 extends Strategy{
    private Direction dir = Direction.UP;
    
    @Override
    public void triggerEvent(Event e) {
        return;
//        int shaftNumber = e.getShaftNr();
//        ButtonSensor payload = e.getPayload();
//        BST eventType = payload.getType();
//        int floor = payload.getFloor();
//        Elevator elevator = null; // Elevator related to event
//        
//        for(Elevator ele: elevatorList){if(ele.getShaftNr() == shaftNumber) elevator = ele;} // Find the corresponding elevator
//        
//        if (eventType == BST.FLOORSENSOR) { // Elevator reaches a floor
//            // Step 1: Check if callRequest is fulfilled.
//            for(CallRequest req : callRequests){
//                if(req.getDestination() == floor){
//                    callRequests.remove(req);                    
//                    elevator.changeState(State.OPENING_DOORS);
//                    // automatically enters state DOORS_OPEN & CLOSING DOORS & IDLE
//                }
//            }
//            // Step 2: Check if targetRequest is fulfilled.
//            for(TargetRequest req : targetRequests )
//                if(req.getDestination() == floor){
//                    targetRequests.remove(req);
//                    elevator.changeState(State.OPENING_DOORS);
//                    // automatically enters state DOORS_OPEN & CLOSING DOORS & IDLE
//                }
//            if(floor == 0){ // Elevator reaches bottom floor
//                dir = Direction.UP;
//                if(!callRequests.isEmpty() || !targetRequests.isEmpty()){
//                    // Any request left? -> If yes, continue.
//                    elevator.goTo(3);
//                }
//            }
//            if(floor == 3){ // Elevator reaches top floor
//                dir = Direction.DOWN;
//                if(!callRequests.isEmpty() || !targetRequests.isEmpty()){
//                    // Any request left? -> If yes, continue.
//                    elevator.goTo(0);
//                }
//            }
//        }
        
        

        // Event 1: No more requests.
        
        // Event 2: First request.
        
        // Event 3: Elevator reaches top floor.
        
        // Event 4: Elevator reaches bottom floor.
    }
    
    /**
     * 
     */
    public void start(){
        System.out.println("STARTED THREAD!");
        Thread t = new Thread(){
            @Override
            public void run() {
                while (true) {
                    for(Elevator e: elevatorList){
                        
                        ArrayList<Request> requestsToVisit = new ArrayList<>();
                        ArrayList<Integer> floorsToVisit = new ArrayList<>();
                        if(!targetRequests.isEmpty() || !callRequests.isEmpty()){ // Only move if there are requests.
                            // Step 1: Check for existing requests and go there
                            for(TargetRequest r : targetRequests){
                                floorsToVisit.add(r.getDestination());
                                requestsToVisit.add(r);
                            }
                            for(CallRequest r : callRequests){
                                floorsToVisit.add(r.getDestination());
                                requestsToVisit.add(r);
                            }
                            for(int floor : floorsToVisit){
                                e.goTo(floor);
                            }
                            targetRequests.removeAll(requestsToVisit); // Necessary to avoid ConcurrentModificationException
                            callRequests.removeAll(requestsToVisit);
                            requestsToVisit.clear();
                            floorsToVisit.clear();
                            
                            // Step 2: Move elevator to top floor
                            while(e.getCurrentFloor() != 3){
                                e.goTo(3);
                                dir = Direction.DOWN;
                            }
                        
                            //Step 3: Check for existing requests and go there
                            for(TargetRequest r : targetRequests){
                                floorsToVisit.add(r.getDestination());
                                requestsToVisit.add(r);
                            }
                            for(CallRequest r : callRequests){
                                floorsToVisit.add(r.getDestination());
                                requestsToVisit.add(r);
                            }
                            for(int floor : floorsToVisit){
                                e.goTo(floor);
                            }
                            targetRequests.removeAll(requestsToVisit);
                            callRequests.removeAll(requestsToVisit);
                            requestsToVisit.clear();
                            floorsToVisit.clear();

                            // Step 4: Move elevator to bottom floor
                            while(e.getCurrentFloor() != 0){
                                e.goTo(0);
                                dir = Direction.UP;
                            }
                        }
                    }
                }
            }
        };
        t.start();
    }

    @Override
    public void setElevators(ArrayList<Elevator> elevators) {
        super.setElevators(elevators);
        start();
    }
    
    
}
