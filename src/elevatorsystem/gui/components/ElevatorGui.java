package elevatorsystem.gui.components;

import elevatorsystem.gui.util.GuiController;
import elevatorsystem.io.BST;
import elevatorsystem.io.ButtonSensor;
import elevatorsystem.io.ElevatorOutBit;
import elevatorsystem.io.OT;
import elevatorsystem.requests.TargetRequest;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import nl.fontys.sevenlo.hwio.BitListener;

/**
 *
 * @author Sahin
 */
public class ElevatorGui extends JFrame implements Observer {

    private Shaft shaft;
    private Cage cage;
    private JPanel panel;
    private JPanel requestButtons;
    private FloorGUI fgui;
    private Map<String, Floor> floors;
    private GuiController controller;

    public ElevatorGui() {
        super("Elevator");
        init();
        start();
    }

    private void start() {
        configure();
    }

    private void init() {
        fgui = new FloorGUI();
        floors = fgui.getFloors();
        controller = new GuiController(floors);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setBounds(new Rectangle(1200, 1200));
        this.setMinimumSize(new Dimension(600, 1200));
        shaft = new Shaft();
        cage = new Cage();
        shaft.setCage(cage);
        panel = new JPanel();
        requestButtons = fgui.getRequestButtons();
    }

    private void configure() {
        System.out.println("configuring");
        for (Floor f : floors.values()) {
            panel.add(f.getSensors());
        }
        Sensors sensors = cage.getDoor().getSensors();

        panel.add(shaft);
        panel.add(cage);
        cage.add(sensors);
        this.add(panel);

        shaft.add(requestButtons);

        requestButtons.setBounds(new Rectangle(100, 800));
        requestButtons.setOpaque(false);
        requestButtons.setLocation(16, 20);
        requestButtons.setLayout(null);

        sensors.setBounds(new Rectangle(50, 20));
        sensors.setLocation(50, 20);

        panel.setLayout(null);
        panel.setBounds(new Rectangle(800, 1200));
        shaft.setLocation(0, 0);
        cage.setLocation(71,506);

        this.pack();
        
    }

    public Cage getCage() {
        return cage;
    }

    public Shaft getShaft() {
        return shaft;
    }
    
    public FloorGUI getFloorGUI(){
        return fgui;
    }
    
    public GuiController getController(){
        return controller;
    }
    int lastfloor = -1;
    public int getCurrentFloor(){
        try{
            int floor = controller.getCurrentFloor(cage).getFloor();
            lastfloor = floor;
            return floor;
        }
        catch(Exception ex){
            //elevator is moving
            return lastfloor;
        }

    }



    @Override
    public void update(Observable o, Object arg) {
        System.out.println("");
        if(arg.equals("openDoor")){
            controller.open(cage);
        }
        if(arg.equals("closeDoor")){
            controller.close(cage);
        }
        
        if (arg.toString().contains("floor")) {
            String floor = arg.toString().split(" ")[1];
            Floor fl = floors.get("f" + floor);
            controller.moveTo(cage, fl );
        }
    }
}
