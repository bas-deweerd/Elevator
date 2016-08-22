/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elevatorsystem.io;

import nl.fontys.sevenlo.hwio.InBit;

/**
 * Button Sensor.
 *
 * @author hom
 */
public class ButtonSensor extends InBit {

    private final BST type;
    private final int floor;

    /**
     * Create a ButtonSensor for a bit with type and id.
     *
     * @param bitNr bit number
     * @param t     button sensor type
     * @param f     floor id
     */
    public ButtonSensor(int bitNr, BST t, int f) {
        super(bitNr);
        type = t;
        floor = f;
    }

    @Override
    public String toString() {
        return type + " floor " + floor;
    }

    /**
     * Get floor info.
     *
     * @return floor info.
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Return the button type.
     *
     * @return the type.
     */
    public BST getType() {
        return type;
    }
}
