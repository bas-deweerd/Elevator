/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevatorsystem.strategy;

import elevatorsystem.elevator.Elevator;
import elevatorsystem.event.Event;
import elevatorsystem.io.BST;
import elevatorsystem.io.ButtonSensor;
import elevatorsystem.requests.TargetRequest;
import elevatorsystem.state.State;
import static elevatorsystem.state.State.OPENING_DOORS;
import elevatorsystem.util.Direction;
import static elevatorsystem.util.Direction.DOWN;
import static elevatorsystem.util.Direction.UP;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Single elevator strategy for the full pater noster.
 * @author Bas
 */
public class FPNStrategy3 extends Strategy{
    Direction dir = Direction.UP;
    Elevator elevator;
    private final ExecutorService ex = Executors.newCachedThreadPool();
    private final ExecutorCompletionService<FPNCallable> ex2 = new ExecutorCompletionService(ex);
    
    @Override
    public void triggerEvent(Event event) {
        BST type = event.getEventType();
        int floor = event.getPayload().getFloor();
        elevator = elevatorList.get(0);
        System.out.println("Event is triggered");
        switch(type){
            case FLOORSENSOR:{
                System.out.println("Floor sensor. Floor = " + floor);
                if(areThereAnyRequestsOnFloor(floor)){
                    // If so, open door & remove request.
                    elevator.changeState(OPENING_DOORS);
                    removeRequestsOfFloor(floor);
                }
                
                // Top or bottom floor reached? -> change direction
                if(floor == 0 || floor == 3){
                    changeDirection();
                }
                
                // Is there any work left to do?
                if(!targetRequests.isEmpty()){
                    // If so, find the next floor.
                    int nextFloor = getNextFloor(floor);
                    System.out.println("Next floor = " + nextFloor);
                    
                    // GO TO NEXT FLOOR
                    // Attempt 1:
                    triggerEvent(new Event(new ButtonSensor(12, BST.TARGETBUTTON, nextFloor), event.getShaftNr()));
                    
                    //Attempt 2:
//                    FPNCallable task = new FPNCallable(elevator, nextFloor);
//                    Thread t = new Thread() {
//                        @Override
//                        public void run() {
//                            Future future = ex2.submit(task);
//                            try {
//                                future.get();
//                                System.out.println("future was completed");
//                            } catch (InterruptedException ex) {
//                                Logger.getLogger(FPNStrategy3.class.getName()).log(Level.SEVERE, null, ex);
//                            } catch (ExecutionException ex) {
//                                Logger.getLogger(FPNStrategy3.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                        }
//                    };
//                    t.start();
                    
                }
                // If not remain IDLE
                break;
            }
            
            case TARGETBUTTON:{
                System.out.println("Targetbutton event is triggered.");
                int currentFloor = elevator.getCurrentFloor();
                int nextFloor = getNextFloor(currentFloor);
                System.out.println("Current floor = " + currentFloor);
                System.out.println("Next floor = " + nextFloor);
                FPNCallable task = new FPNCallable(elevator, nextFloor);
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        Future future = ex2.submit(task);
                        try {
                            future.get();
                            System.out.println("future was completed");
                        } catch (InterruptedException ex) {
                            Logger.getLogger(FPNStrategy3.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ExecutionException ex) {
                            Logger.getLogger(FPNStrategy3.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
                t.start();
                break;
            }
        }
    }
    
    private void removeRequestsOfFloor(int floor){
        targetRequests.removeIf(r -> r.getDestination() == floor);
    }
    
    private void changeDirection(){
        dir = dir==UP ? DOWN:UP; 
    }
    
    private int getNextFloor(int currentFloor){
        int nextFloor;
        if(dir==UP){
            nextFloor = currentFloor + 1;
        }
        else{ // dir==DOWN
            nextFloor = currentFloor - 1;
        }
        return nextFloor;
    }
 
    private boolean areThereAnyRequestsOnFloor(int floor){
        return targetRequests.stream().anyMatch(r -> r.getDestination() == floor);
    }
}
