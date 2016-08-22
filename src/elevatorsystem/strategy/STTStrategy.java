/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevatorsystem.strategy;

import elevatorsystem.elevator.Elevator;
import elevatorsystem.event.Event;
import elevatorsystem.requests.CallRequest;
import elevatorsystem.requests.Request;
import elevatorsystem.requests.TargetRequest;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Joey
 */
public class STTStrategy extends Strategy{

/*Multicage --> Shortest Travel Time*/    
    
    private ArrayList<Elevator> elevatorList = new ArrayList<>();

    @Override
    public void addTargetRequest(TargetRequest r) {
        targetRequests.add(r);
        targetBehaviour();
    }

    @Override
    public void addCallRequest(CallRequest r) {
        callRequests.add(r);
        callBehaviour();
        
    }
    
    private void targetBehaviour(){
            //What if there are multiple requests at the same time
            //Handling all target requests, sorted on shaft number
            ArrayList<TargetRequest> sortedTargetList = new ArrayList<>();

            //Makes a list of targetRequests sorted on distance, last element = closest, currently still wrong
            Comparator c = new Comparator(){

                    @Override
                    public int compare(Object o1, Object o2) {
                       int value;
                        
                       TargetRequest t1 = (TargetRequest) o1;
                       TargetRequest t2 = (TargetRequest) o2;
                       int t1distance = Math.abs(t1.getDestination() - elevatorList.get(t1.getShaftNr()).getCurrentFloor());
                       int t2distance = Math.abs(t2.getDestination() - elevatorList.get(t2.getShaftNr()).getCurrentFloor());
                       
                       return t1distance - t2distance;
                    }
                };
                
                //sortedTargetList = targetRequests;
                sortedTargetList.sort(c);
           
            //Go to closest destination, and remove it from the list
            //(currently does not support multiple targets going to the same floor correctly, because multiple targets can have the same floor, which should not be possible)
            for(int i = 0; i < sortedTargetList.size(); i++){
                
                TargetRequest closestRequest = sortedTargetList.get(0);
                Elevator targetElevator = elevatorList.get(closestRequest.getShaftNr());
                targetElevator.goTo(closestRequest.getDestination());
                targetRequests.remove(sortedTargetList.get(0));
                sortedTargetList.remove(0);
            }
        }
    
    private void callBehaviour(){
        //Handling all call requests, determing the best for the job.
            int requestedFloor;
          
            CallRequest cr = callRequests.get(callRequests.size() -1);
            
            //Look at the floor to which the closest cage needs to go
            requestedFloor = cr.getDestination();
            Elevator closestElevator = elevatorList.get(0); 
            int callMinDistance = 999; 
            
            for(Elevator e: elevatorList){
                //Look at the floor at which the cages currently are
                int currentFloor = e.getCurrentFloor();
                //Calculate the distance between the elevator and the floor
                int distance = Math.abs(requestedFloor - currentFloor);
                //Calculate which elevator has the least distance
                if(distance < callMinDistance){
                    callMinDistance = distance;
                    closestElevator = e;
                }  
            }
            closestElevator.goTo(requestedFloor);
            callRequests.remove(cr);
    }
        

        
    

    @Override
    public void triggerEvent(Event e) {
        //Actual state stuff happening in here
    }

    @Override
    public void setElevators(ArrayList<Elevator> elevators) {
        this.elevatorList = elevators;
    }
    
}
