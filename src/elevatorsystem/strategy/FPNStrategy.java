/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevatorsystem.strategy;

import elevatorsystem.elevator.Elevator;
import elevatorsystem.event.Event;
import elevatorsystem.io.BST;
import elevatorsystem.requests.CallRequest;
import elevatorsystem.requests.TargetRequest;
import elevatorsystem.state.State;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joey
 */
public class FPNStrategy extends Strategy {

    /*Full Pater Noster: Always make a complete circular movement between lowest and highest floor.   
     That is:  reverse direction of the elevator only at the top or bottom floor. 
     The movement  stops  if  there  are  no  more  requests  in  the  forward  circular  direction. 
     On arrival of request the movement is resumed in the same direction.*/
    // when call request, go in direction dir. if destination reached stop and visit.
    // when target request, go in direction dir. if destination reached stop and visit.
    // if cage reached top, set direction to DOWN. setDir("DOWN");
    // if cage reached bot, set direction to UP.   setDir("UP");
    private ArrayList<Elevator> elevatorList = new ArrayList<>();
    private ArrayList<CallRequest> nextToVisit = new ArrayList<>();
    private int nextDest = 0;
    private String dir = "UP";
    private Elevator e;

    Comparator<CallRequest> comp = new Comparator<CallRequest>() {

            @Override
            public int compare(CallRequest o1, CallRequest o2) {
                int result = 0;
                if (o1.getDestination() < o2.getDestination()) {
                    result = -1;
                }
                if (o1.getDestination() == o2.getDestination()) {
                    result = 0;
                }
                if (o1.getDestination() > o2.getDestination()) {
                    result = 1;
                }
                return result;
            }
        };
    
    Comparator<CallRequest> compInv = new Comparator<CallRequest>() {

            @Override
            public int compare(CallRequest o1, CallRequest o2) {
                int result = 0;
                if (o1.getDestination() < o2.getDestination()) {
                    result = 1;
                }
                if (o1.getDestination() == o2.getDestination()) {
                    result = 0;
                }
                if (o1.getDestination() > o2.getDestination()) {
                    result = -1;
                }
                return result;
            }
        };
    
    
    @Override
    public void addTargetRequest(TargetRequest r) {
        targetRequests.add(r);
        e = elevatorList.get(0);
    }

    @Override
    public void addCallRequest(CallRequest r) {
        callRequests.add(r);
        e = elevatorList.get(0);
        callElevator();
    }

    @Override
    public void triggerEvent(Event e) {
//        System.out.println("Trigger Event");
//        if (e.getEventType() == BST.FLOORSENSOR) {
//            System.out.println("BIER");
//            Elevator el = this.e;
//            Thread t = new Thread() {
//                @Override
//                public void run() {
//                    try {
//
//                        Thread.sleep(8000);
//                        if (!nextToVisit.isEmpty() && nextDest == e.getPayload().getFloor()) {
//                            nextDest = nextToVisit.get(0).getDestination();
//                            el.goTo(nextToVisit.remove(0).getDestination());
//                        } else if (el.getCurrentFloor() == 3 || el.getCurrentFloor() == 0) {
//                            callElevator();
//                        }
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(FPNStrategy.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            };
//            t.start();
//
//        }
    }

    @Override
    public void setElevators(ArrayList<Elevator> elevators) {
        this.elevatorList = elevators;
    }

    public void callElevator() {
        System.out.println("entered method=============================");
        if (e.getCurrentFloor() == 3) {
            dir = "DOWN";
        }
        if (e.getCurrentFloor() == 0) {
            dir = "UP";
        }
        System.out.println("Dir is: " + dir);
        

        if (e.getState() == State.MOVING) {
            System.out.println("in moving==========");
            if (dir.equals("UP")) {
                if (callRequests.get(callRequests.size() - 1).getDestination() > e.getCurrentFloor()) {
                    nextToVisit.add(callRequests.get(callRequests.size() - 1));
                    nextToVisit.sort(comp);
                    callRequests.remove(callRequests.size() - 1);
                }
            } else {
                if (callRequests.get(callRequests.size() - 1).getDestination() < e.getCurrentFloor()) {
                    nextToVisit.add(callRequests.get(callRequests.size() - 1));
                    nextToVisit.sort(compInv);
                    callRequests.remove(callRequests.size() - 1);
                }
            }
            return;
        }

        ArrayList<CallRequest> requestsToRemove = new ArrayList<>();
        if (dir.equals("UP")) {
            System.out.println("in UP");
            for (CallRequest c : callRequests) {
                if (c.getDestination() > e.getCurrentFloor()) {
                    nextToVisit.add(c);
                    requestsToRemove.add(c);
                }
            }
            if (!nextToVisit.isEmpty()) {
                nextDest = nextToVisit.get(0).getDestination();
                nextToVisit.sort(comp);
                callRequests.removeAll(requestsToRemove);
                e.goTo(nextToVisit.remove(0).getDestination());
            }
        } else {
            System.out.println("in DOWN");
            for (CallRequest c : callRequests) {
                if (c.getDestination() < e.getCurrentFloor()) {
                    nextToVisit.add(c);
                    requestsToRemove.add(c);
                }
            }
            if (!nextToVisit.isEmpty()) {
                nextDest = nextToVisit.get(0).getDestination();
                nextToVisit.sort(compInv);
                callRequests.removeAll(requestsToRemove);
                e.goTo(nextToVisit.remove(0).getDestination());
            }
        }
    }

}
