/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevatorsystem.strategy;

import elevatorsystem.elevator.Elevator;
import elevatorsystem.event.Event;
import elevatorsystem.requests.CallRequest;
import elevatorsystem.requests.TargetRequest;
import java.util.ArrayList;

/**
 *
 * @author Joey
 */
public class SPNStrategy extends Strategy {
    
/* Skipping Pater Noster: The direction may be reversed as soon as there are no more requests
in the current direction.  This avoids going to the extreme floors if there are no request
from or to those floors. 
If there are no more requests or targets to visit, the cage can stay at the floor it is visiting.*/

    @Override
    public void addTargetRequest(TargetRequest r) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addCallRequest(CallRequest r) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void triggerEvent(Event e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setElevators(ArrayList<Elevator> elevators) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
