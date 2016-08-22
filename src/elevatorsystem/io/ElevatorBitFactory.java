/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package elevatorsystem.io;

import nl.fontys.sevenlo.hwio.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Properties;

/**
 * Factory example. Shows how input bits can be created of a subtype and
 * connected to a listener.
 * <p>
 * This version connects the listener to both input and outputs.
 *
 * @author hom
 */
public class ElevatorBitFactory implements AbstractBitFactory, BitListener {

    private final int pinCount;
    /**
     * Storage for the created outbits.
     */
    private final EnumMap<OT, ArrayList<OutBit>> outMap;
    private Properties props;

    /**
     * At construction time, pass the listener to all input bits.
     *
     * @param props Property description of elevatorsystem.io.
     */
    public ElevatorBitFactory(Properties props) {
        this.props = props;
        String pinCountS = props.getProperty("pinCount", "0");
        this.pinCount = Integer.parseInt(pinCountS);
        System.out.println("found pinCount " + pinCount);
        outMap = new EnumMap<>(OT.class);
        for (OT ot : OT.values()) {
            outMap.put(ot, new ArrayList<>());
        }
    }

    /**
     * See AbstractBitFactory doc. Creates the input bits for the elevatorsystem.elevator.
     *
     * @param in    input port
     * @param bitNr bit number
     *
     * @return the Bit
     */
    @Override
    public Bit createInputBit(Input in, int bitNr) {
        InBit bit = null;
        String pinName = "pin" + bitNr;
        String bitdescription = props.getProperty(pinName);
        String[] bitparts = bitdescription.split(",");
        String io = bitparts[0];
        String role = bitparts[1];
        int nrInRole = 0;
        if (bitparts.length > 2) {
            nrInRole = Integer.parseInt(bitparts[2]);
        }
        BST t = BST.valueOf(BST.class, role.toUpperCase());
        bit = new ButtonSensor(bitNr, t, nrInRole);
        //bit.addListener(this);
        return bit;
    }

    /**
     * Creates output bits for the elevatorsystem.elevator.
     *
     * @param port  output
     * @param bitNr bit number.
     *
     * @return the created Bit
     */
    @Override
    public final Bit createOutputBit(Output port, int bitNr) {
        ElevatorOutBit bit = null;
        String pinName = "pin" + bitNr;
        String bitdescription = props.getProperty(pinName);
        String[] bitparts = bitdescription.split(",");
        String io = bitparts[0];
        String role = bitparts[1];
        int nrInRole = 0;
        if (bitparts.length > 2) {
            nrInRole = Integer.parseInt(bitparts[2]);
        }
        OT type = OT.valueOf(role.toUpperCase());
        bit = new ElevatorOutBit(port, bitNr, type, nrInRole);
        outMap.get(type).add(nrInRole, bit);
        //bit.addListener(this);
        return bit;
    }

    /**
     * Two bits for motor.
     *
     * @param port for output.
     *
     * @return the motor bit group.
     */
    public BitGroup createMotorBitGroup(Output port) {
        //int motorUpBit = findOutputByName("out.motorup.0");
        int motorDownBit = findOutputByName("out.motordown.0");
        return new OutBitGroup(port, 2, motorDownBit);
    }

    private int findOutputByName(String name) {
        int result = -1;
        for (int i = 0; i < this.pinCount; i++) {
            String propName = "pin" + i;
            if (null != this.props.getProperty(propName)) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * Four bits for indicator bits.
     *
     * @param port for output
     *
     * @return bit group
     */
    public BitGroup createIndicatorBitGroup(Output port) {
        int indicatorLeftLed = findOutputByName("out.floorindicator.3");
        return new OutBitGroup(port, 4, indicatorLeftLed);
    }

    /**
     * Update the bit to newvalue.
     *
     * @param bit      the bit to update.
     * @param newValue the news.
     */
    @Override
    public void updateBit(Object bit, boolean newValue) {
        System.out.println("updater " + bit + " = " + newValue);
        OutBit ob = null;

        if (bit instanceof ButtonSensor) {
            ButtonSensor bs = (ButtonSensor) bit;
            int floor = bs.getFloor();
            switch (bs.getType()) {
                case DOOROPENBUTTON:
                    System.out.println("open button");
                    ob = getOutBit(OT.DOORMOTOROPEN);
                    System.out.println("outbit=" + ob);
                    break;
                case DOORCLOSEBUTTON:
                    ob = getOutBit(OT.DOORMOTORCLOSE);
                    break;
                case TARGETBUTTON:
                    ob = getOutBit(OT.FLOORINDICATOR, floor);
                    break;
                case NURSEBUTTON:
                    ob = getOutBit(OT.MOTORUP);
                    break;
                case OBSTRUCTIONSENSOR:
                    ob = getOutBit(OT.MOTORDOWN);
                    break;
                case UPBUTTON:
                    ob = getOutBit(OT.UPLED);
                    break;
                case DOWNBUTTON:
                    ob = getOutBit(OT.DOWNLED);
                    break;
                case FLOORSENSOR:
                    OutBit md = getOutBit(OT.MOTORDOWN);
                    OutBit mu = getOutBit(OT.MOTORUP);
                    if (floor == 0) {
                        md.set(false);
                        mu.set(true);
                    } else if (floor == 3) {
                        mu.set(false);
                        md.set(true);
                    }
                    break;
                default:
                    break;
            }
            if (null != ob) {
                System.out.println("set output " + ob + " to " + newValue);
                ob.set(newValue);
            }
        }
    }

    /**
     * Get OutBit of Type at index 0.
     *
     * @param type of the bit
     *
     * @return the requested bit.
     */
    public OutBit getOutBit(OT type) {
        return getOutBit(type, 0);
    }

    /**
     * Get OutBit by type and number (floor).
     *
     * @param type   the OT type
     * @param number the floor number
     *
     * @return the requested outbit
     *
     * @throws ArrayIndexOutOfBoundsException when bit not available.
     */
    public OutBit getOutBit(OT type, int number) {
        return outMap.get(type).get(number);
    }
}
