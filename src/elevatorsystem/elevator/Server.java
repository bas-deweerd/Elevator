package elevatorsystem.elevator;

import elevatorsystem.event.Event;
import elevatorsystem.gui.components.ElevatorGui;

import elevatorsystem.io.ElevatorBitFactory;
import elevatorsystem.requests.CallRequest;
import elevatorsystem.requests.Request;
import elevatorsystem.requests.TargetRequest;
import elevatorsystem.simulation.GUIWarrior;
import elevatorsystem.strategy.*;
import nl.fontys.sevenlo.hwio.Bit;
import nl.fontys.sevenlo.hwio.PollThreads;
import nl.fontys.sevenlo.hwio.Poller;
import nl.fontys.sevenlo.hwio.SwingPoller;
import nl.fontys.sevenlo.iowarrior.IOWarrior;
import nl.fontys.sevenlo.iowarrior.IOWarriorConnector;
import nl.fontys.sevenlo.utils.ResourceUtils;
import nl.fontys.sevenlo.widgets.IOGUIPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

/**
 * This class was created by timwedde on 19.11.15.
 */
public class Server extends Thread implements Observer {

    private ArrayList<Elevator> elevators = new ArrayList<>();
    private ArrayList<IOWarrior> warriors = new ArrayList<>();
    private Strategy strategy;
    private Strategy[] strategies = {new FPNStrategy3(), new SimpleStrategy2()};

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    /**
     * Sets the strategy for the elevator system. For changing strategy during runtime,
     * use the changeStrategy() method.
     *
     * @param strategy The strategy to set
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
        strategy.setElevators(elevators);
        
    }


    /**
     * Setup and execution of the whole system. The server will scan for and instantiate
     * all found IOWarriors as well as create a number of virtual lifts. It will also set up
     * the event chain and start a polling thread for each chip.
     */
    @Override
    public synchronized void run() {
        System.out.println("I'm running!");
        //Load properties file and get necessary information
        Properties prop = new Properties();
        prop = ResourceUtils.loadPropertiesFormFile(prop, "elevator.properties");
        int inputMask = ResourceUtils.getInputMaskFromProperties(prop, 32);

        //Use IOWarrior helper classes to get all available chips
        try {
            IOWarriorConnector iowc = IOWarriorConnector.getInstance();
            //For every chip there is an elevatorsystem.elevator, so prepare that
            for (int i = 0; i < iowc.getWarriorCount(); i++) {
                long handle = iowc.getHandle(i); //Get handle
                IOWarrior warrior = new IOWarrior(handle, inputMask, new ElevatorBitFactory(prop)); //Make IOWarrior instance
                warrior.connect(); //Necessary setup
                Poller poller = new SwingPoller(warrior);
                Thread pollThread = PollThreads.createPollThread(poller);
                pollThread.start();

                IOGUIPanel iogp = new IOGUIPanel("HW-Control", warrior, prop);
                iogp.startTheShow();

                warriors.add(warrior); //Archive
                //                ElevatorGUI egui = new ElevatorGUI();



                Elevator e = new Elevator(warrior); //Create elevator
                e.startUp();
                elevators.add(e); //Archive that as well
                e.addObserver(this); //Add the server as an observer
                //                e.addObserver(gw);
                //Subscribe the elevatorsystem.elevator to necessary events, let the elevator class deal with that shit
                for (int j = 0; j < 32; j++) {
                    //we only need these bits
                    if((j<16) || (j == 24) || (j>= 28)){
                    Bit b = (Bit) warrior.getBit(j);
                    b.addListener(e);
                    }
                }

                System.out.println("Added another elevator successfully!");
            }
        } catch (Throwable t) {
            System.out.println("No IOWarrior library found, skipping hardware control...");
        }

        //Here we have the option to add some "fake" elevators that are simulated instead of being chip-controlled
        for (int i = 0; i < 1; i++) {
            System.out.println("Creating fake elevator...");
            ElevatorGui egui = new ElevatorGui();
                        GUIWarrior guiWarrior = new GUIWarrior(inputMask, prop, egui);
                        guiWarrior.set(egui);
                        
                        // let application know that it's simulating
                        // this is done for floor sensors etc.
                        
            //SimpleGUIWarrior guiWarrior = new SimpleGUIWarrior(inputMask, prop);
            Elevator e = new Elevator(guiWarrior);
            e.setSimulation(true);
            elevators.add(e);
            e.addObserver(egui);
                        e.addObserver(this);
            for (int j = 0; j < 16; j++) {
                if((j <16) ||(j == 24)||(j >27)){
                    Bit b = (Bit) guiWarrior.getBit(j);
                    b.addListener(e);
                }
            }
            
            e.startUp();
        }
        System.out.println("I'm managing " + elevators.size() + (elevators.size() < 2 ? " elevator" : " elevators") + " right now!");
        
        //init strategy to the right one
        changeStrategy(strategies[0]);
        createDashboard();
    }

    /**
     * Creates a simple GUI to keep the server running and display the status of the system.
     */
    private void createDashboard() {
        //Maybe add a strategy selection here...
        JFrame dashboard = new JFrame();
        dashboard.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = dashboard.getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel topPane = new JPanel();
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.PAGE_AXIS));
        topPane.add(new JLabel("Status: Up and running!"));
        topPane.add(new JLabel("Currently running Elevators: " + elevators.size()));
        JLabel strategyLabel = new JLabel("Current strategy: " + strategy.getClass().getSimpleName());
        topPane.add(strategyLabel);
        for (Elevator e : elevators) {
            JLabel l = new JLabel("Elevator " + e.getShaftNr() + ": " + e.getState());
            e.giveLabel(l);
            topPane.add(l);
        }
        contentPane.add(topPane);

        JButton close = new JButton("Close & Exit");
        close.addActionListener(e -> System.exit(0));

        JComboBox<Strategy> strategyList = new JComboBox<>(strategies);
        strategyList.setSelectedIndex(0);
        strategyList.addActionListener(e -> {
            Strategy newStrategy = (Strategy) ((JComboBox) e.getSource()).getSelectedItem();
            strategyLabel.setText("Current strategy: " + newStrategy.getClass().getSimpleName());
            changeStrategy(newStrategy);
        });

        contentPane.add(topPane, BorderLayout.NORTH);

        contentPane.add(close, BorderLayout.CENTER);
        contentPane.add(strategyList, BorderLayout.SOUTH);

        dashboard.setLocationRelativeTo(null);
        dashboard.pack();
        dashboard.setVisible(true);
    }

    /**
     * Takes care of migrating data from the old to the new strategy.
     * The old strategy is responsible for doing this.
     *
     * @param s The new strategy
     */
    private void changeStrategy(Strategy s) {
        //If it's not the same strategy, switch.
        if (s != strategy && s != null) {
            strategy = s;
            strategy.setElevators(elevators);
        }
    }

    /**
     * Fires everytime an event occurs on any elevator.
     * The server will take care of forwarding appropriate events to the strategy.
     *
     * @param o   The object that triggered the update
     * @param arg The payload
     */
    @Override
    public void update(Observable o, Object arg) {
        Elevator e = (Elevator) o; //This is always an elevator object
        //The argument may be a Request or an Event, so deal with it accordingly
        if(arg instanceof Request){
            if(arg instanceof TargetRequest){
                //If request from inside:
                strategy.addTargetRequest((TargetRequest) arg);
            }
            else if(arg instanceof CallRequest){
                //If request from outside:
                strategy.addCallRequest((CallRequest) arg);
            }
        } else if(arg instanceof Event){
            //Trigger event on the strategy, so it can take over switching states and stuff...
            strategy.triggerEvent((Event) arg);
        }
    }
}
