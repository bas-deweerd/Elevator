package elevatorsystem.gui.util;

import elevatorsystem.gui.components.*;

/**
 *
 * @author merve
 */
public interface ElevatorOperation {

    public void close(Cage cage);
    
    public void open(Cage cage);
    
    public void obstruct();
    
    public void moveUp(Cage cage);
    
    public void moveDown(Cage cage);
    
    public void moveTo(Cage cage, Floor floor);
    
    public Floor getCurrentFloor(Cage cage);
    
    public void shutDown(Cage cage);
    
    public void call(Cage cage, Floor floor);
    
    public void stop(Cage cage);
}
