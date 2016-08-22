package elevatorsystem.simulation;

import elevatorsystem.elevator.Elevator;
import elevatorsystem.gui.components.Cage;
import elevatorsystem.gui.components.ElevatorGui;
import elevatorsystem.gui.components.Floor;
import elevatorsystem.gui.components.FloorGUI;
import elevatorsystem.gui.util.GuiController;
import elevatorsystem.gui.util.Target;
import elevatorsystem.io.BST;
import elevatorsystem.io.ButtonSensor;
import elevatorsystem.io.ElevatorOutBit;
import elevatorsystem.io.OT;
import elevatorsystem.strategy.SimpleStrategy;
import elevatorsystem.strategy.Strategy;
import nl.fontys.sevenlo.hwio.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

/**
 * This class was created by timwedde on 26.11.15.
 * <p>
 * Simulates the hardware chip by emitting the same events with the difference that they are triggered by buttons.
 * There is (almost)duplicate code from the IOWarrior in here because we can extend it so this workaround will have to do.
 */
public class GUIWarrior extends Observable implements BitAggregate, ActionListener {

   
    public static final int SUPPORTED_BITS = 32;
    private Bit[] bit;
    private FloorGUI buttons;
    private Cage cage;
    private Map<String, Floor> floors;
    private JFrame dashboard;
    private Strategy strategy;
    private ElevatorGui gui;

    /**
     * Creates a panel that controls all elevator functions through buttons.
     *
     * @param im Bit InputMask
     * @param props Properties file to match pins to functions
     * @param gui ElevatorGui to process bit changes
     */
    public GUIWarrior(int im, Properties props, ElevatorGui gui) {
        //buttons = new FloorGUI();
        this.gui = gui;
        bit = new Bit[SUPPORTED_BITS];
        dashboard = new JFrame();
        dashboard.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        strategy = new SimpleStrategy();
        EnumMap<OT, ArrayList<OutBit>> outMap = new EnumMap<>(OT.class);
        for (OT ot : OT.values()) {
            outMap.put(ot, new ArrayList<>());
        }
        for (int i = 0; i < bit.length; i++) {
            if ((im & (1 << i)) != 0) { //Input Bits
                String pinName = "pin" + i;
                String bitdescription = props.getProperty(pinName);
                String[] bitparts = bitdescription.split(",");
                String role = bitparts[1];
                int nrInRole = 0;
                if (bitparts.length > 2) {
                    nrInRole = Integer.parseInt(bitparts[2]);
                }
                BST t = BST.valueOf(BST.class, role.toUpperCase());
                Bit b = new ButtonSensor(i, t, nrInRole);
                bit[i] = b;
                

            } else { //Output bits, just sink their values into a black hole of null
                String pinName = "pin" + i;
                String bitdescription = props.getProperty(pinName);
                String[] bitparts = bitdescription.split(",");
                String role = bitparts[1];
                int nrInRole = 0;
                if (bitparts.length > 2) {
                    nrInRole = Integer.parseInt(bitparts[2]);
                }
                OT type = OT.valueOf(role.toUpperCase());
                OutBit b = new ElevatorOutBit(new Output() {
                    @Override
                    public void writeMasked(int mask, int value) {

                    }

                    @Override
                    public int lastWritten() {
                        return 0;
                    }
                }, i, type, nrInRole);
                outMap.get(type).add(nrInRole, b);
                //bit.addListener(this);
                bit[i] = b;
            }
        }
    }
    
    public int getCurrentFloor(){
        return gui.getCurrentFloor();
    }

    @Override
    public BitOps getBit(int i) {
        return bit[i];
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int getInputMask() {
        return 0;
    }

    @Override
    public int lastRead() {
        return 0;
    }

    @Override
    public void connect() throws IllegalStateException {

    }

    @Override
    public int read() {
        return 0;
    }

    @Override
    public void writeMasked(int mask, int value) {

    }

    @Override
    public int lastWritten() {
        return 0;
    }

    public void set(ElevatorGui e) {
        this.buttons = e.getFloorGUI();
        floors = buttons.getFloors();
        this.cage = e.getCage();
        start();
    }

    private void start() {
        buttons.addListener(this);
        dashboard.add(buttons);
        dashboard.pack();
        dashboard.setVisible(true);
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        Floor requested = null;
        JButton buttonPressed = (JButton) e.getSource();
        if (buttonPressed == buttons.getButtons().get("f0")) {
            requested = floors.get("f0");
            setChanged();
            //notifyObservers(new Target(cage, requested));
            bit[8].set(!bit[8].isSet());
        } else if (buttonPressed == buttons.getButtons().get("f1")) {
            requested = floors.get("f1");
            setChanged();
            //notifyObservers(new Target(cage, requested));
            bit[9].set(!bit[9].isSet());
        } else if (buttonPressed == buttons.getButtons().get("f2")) {
            requested = floors.get("f2");
            setChanged();
            //(new Target(cage, requested));
            bit[10].set(!bit[10].isSet());
        } else if (buttonPressed == buttons.getButtons().get("f3")) {
            requested = floors.get("f3");
            setChanged();
            //notifyObservers(new Target(cage, requested));
            bit[11].set(!bit[11].isSet());
        } else if (buttonPressed == buttons.getButtons().get("open")) {
            setChanged();
            //notifyObservers("open");
            System.out.println("opening doors");
            bit[28].set(!bit[28].isSet());
        } else if (buttonPressed == buttons.getButtons().get("close")) {
            setChanged();
            //notifyObservers("close");
            System.out.println("closing doors");
            bit[30].set(!bit[30].isSet());
        } else if (buttonPressed == buttons.getButtons().get("obstruct")) {
            setChanged();
            //notifyObservers("obstruct");
            System.out.println("obstructed");
            bit[31].set(!bit[31].isSet());
        }
    }




}