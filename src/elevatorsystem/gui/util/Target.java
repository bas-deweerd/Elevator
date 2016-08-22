package elevatorsystem.gui.util;

import elevatorsystem.gui.components.Cage;
import elevatorsystem.gui.components.Floor;

/**
 *
 * @author merve
 */
public class Target {
    private final Cage cage;
    private final Floor floor;
    
    public Target(Cage c, Floor f){
        this.cage = c;
        this.floor = f;
    }

    public Cage getCage() {
        return cage;
    }

    public Floor getFloor() {
        return floor;
    }
    
}
