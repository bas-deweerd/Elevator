package elevatorsystem.gui.util;

import elevatorsystem.gui.components.*;
/**
 *
 * @author merve
 */
public class DoorController {
    
    public DoorController() {
    }
    
    public synchronized void open(Cage cage) {
        cage.getDoor().open();
        cage.getDoor().start();
    }
    
    public synchronized void close(Cage cage) {
        cage.getDoor().close();
        cage.getDoor().start();
        
    }
    
    public synchronized void obstruct(Cage cage) {
        open(cage);
    }
    
}
