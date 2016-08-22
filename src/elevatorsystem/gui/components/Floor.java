package elevatorsystem.gui.components;

import elevatorsystem.gui.util.Position;
import javax.swing.JButton;

/**
 *
 * @author Sahin
 */
public class Floor {
    
    private final int floor;
    private final Position upperLeft;
    private final  Sensors sensors;
    private final JButton callUp;
    private final JButton callDown;

    public Floor(int floor,  Position upperLeft, Sensors s) {
        this.floor = floor;
        this.upperLeft = upperLeft;
        sensors = s;
        callUp = new JButton();
        callDown = new JButton();
    }
    
    public int getFloor() {
        return floor;
    }

    public Position getUpperLeft() {
        return upperLeft;
    }

    public Sensors getSensors() {
        return sensors;
    }

    public JButton getCallUp() {
        return callUp;
    }
    
    public JButton getCallDown(){
        return callDown;
    }
    
    
}
