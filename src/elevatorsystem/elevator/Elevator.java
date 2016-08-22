package elevatorsystem.elevator;

import elevatorsystem.event.Event;
import elevatorsystem.io.BST;
import elevatorsystem.io.ButtonSensor;
import elevatorsystem.requests.CallRequest;
import elevatorsystem.requests.TargetRequest;
import elevatorsystem.simulation.GUIWarrior;
import elevatorsystem.state.State;
import static elevatorsystem.state.State.MOVING;
import elevatorsystem.util.CallDirection;
import nl.fontys.sevenlo.hwio.BitAggregate;
import nl.fontys.sevenlo.hwio.BitListener;
import nl.fontys.sevenlo.hwio.OutBit;

import javax.swing.*;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class was created by timwedde on 19.11.15.
 * <p>
 * This class depicts an elevator. It has state and all necessary operations in
 * it.
 */
public class Elevator extends Observable implements BitListener {

    private static int shaftNrCounter = 0;
    private int shaftNr;
    private State state = State.START_UP;
    private BitAggregate warrior;
    private int currentFloor = 0;
    private boolean enabled = true;

    private OutBit motorDown;
    private OutBit motorUp;
    private OutBit[] indicators = new OutBit[4];
    private OutBit upled;
    private OutBit downled;
    private OutBit doorClose;
    private OutBit doorOpen;
    private Timer timer = new Timer();

    private JLabel label;
    private boolean simulation = false;

    /**
     * Initializes the hardware by moving to the lowest floor and thus creating
     * a concrete state.
     *
     * @param warrior The input tied to this hardware
     */
    public Elevator(BitAggregate warrior) {
        if(warrior instanceof GUIWarrior){
            simulation = true;
        }
        shaftNr = shaftNrCounter;
        shaftNrCounter++;
        this.warrior = warrior;
        assignBits();
        
    }
    
    public void startUp(){
       changeState(State.START_UP);

        Thread t = new Thread() {
            @Override
            public void run() {
                if (!warrior.getBit(6).isSet()) {
                    System.out.println("Waiting...");
                    try {
                        Thread.sleep(1700);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                initFloor();
                changeState(elevatorsystem.state.State.OPENING_DOORS);
            }
        };
        t.start(); 
    }
    
    /**
     * Brings the elevator down to the ground floor if necessary.
     * If not elevator is already on ground floor
     */
    private void initFloor(){
        
        if(!simulation){
            if(!warrior.getBit(12).isSet()){
                motorDown.set(true);
                while (!warrior.getBit(12).isSet()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                motorDown.set(false);
            }
            currentFloor = 0;
        }else{
            currentFloor = 0;
        }
    }

    /**
     * Matches bits from the hardware to variables to be used in the elevator.
     */
    private void assignBits() {
        motorDown = (OutBit) warrior.getBit(20);
        motorUp = (OutBit) warrior.getBit(21);
        doorClose = (OutBit) warrior.getBit(27);
        doorOpen = (OutBit) warrior.getBit(22);
        indicators[0] = (OutBit) warrior.getBit(16);
        indicators[1] = (OutBit) warrior.getBit(17);
        indicators[2] = (OutBit) warrior.getBit(18);
        indicators[3] = (OutBit) warrior.getBit(19);
        upled = (OutBit) warrior.getBit(25);
        downled = (OutBit) warrior.getBit(26);
    }

    /**
     * Handles the entry and exit actions of changing state in this elevator.
     *
     * @param next The next state
     */
    public void changeState(State next) {
        state.exit(this);
        state = next;
        state.entry(this);
        if (label != null) {
            label.setText("Elevator " + getShaftNr() + ": " + getState());
        }
    }



    /**
     * @return The shaft number of this elevator
     */
    public int getShaftNr() {
        return shaftNr;
    }

    /**
     * @return The current state
     */
    public State getState() {
        return state;
    }

    /**
     * Will move the physical cage to the specified floor. Serves as abstraction
     * of the movement process and handles state change for this part.
     *
     * @param floor The floor to go to
     */
    public void goTo(int floor) {
//        if (state != State.MOVING) changeState(State.CLOSING_DOORS);
        if(!simulation){
            OutBit currentMotor;
            if (floor == currentFloor) {
                changeState(State.OPENING_DOORS);
                return;
            } else if (floor > currentFloor) {
                currentMotor = motorUp;
                indicateUp();
            } else {
                currentMotor = motorDown;
                indicateDown();
            }
            Thread t = new Thread() {
                @Override
                public void run() {
                    System.out.println("NOW TRIGGERING FLOOR!");
                    setChanged();
                    //notifyObservers(new MoveAction(floor));
                    try {
                        while (state != elevatorsystem.state.State.MOVING) {
                            Thread.sleep(1);
                        }
                        currentMotor.set(true);
                        while (floor != currentFloor) {
                            Thread.sleep(10);
                        }
                        currentMotor.set(false);
                        indicateIdle();
                        changeState(elevatorsystem.state.State.OPENING_DOORS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        }
        if(simulation){
            setChanged();
            
            if(state == State.IDLE){          
                changeState(MOVING);
            }
            else{
                changeState(State.CLOSING_DOORS);
                
                while(state != State.IDLE){
                    
                }
                
                changeState(MOVING);
                
            }
            notifyObservers("floor " + floor);    
            
            GUIWarrior warri = (GUIWarrior) warrior;
            
            
            while(currentFloor != floor){
                try {
                    if(currentFloor != warri.getCurrentFloor()){
                        //updateBit(new ButtonSensor(12, BST.FLOORSENSOR, currentFloor), false);
                        currentFloor = warri.getCurrentFloor();
                        updateBit(new ButtonSensor(12, BST.FLOORSENSOR, currentFloor), true);
                    }
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Elevator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //updateBit(new ButtonSensor(12, BST.FLOORSENSOR, currentFloor), true);
            //changeState(elevatorsystem.state.State.OPENING_DOORS);
        }
    }

    /**
     * Activates the "up" LED and deactivates the "down" one.
     */
    public void indicateUp() {
        downled.set(false);
        upled.set(true);
    }

    /**
     * Activates the "down" LED and deactivates the "up" one.
     */
    public void indicateDown() {
        upled.set(false);
        downled.set(true);
    }

    /**
     * Deactivates either LED.
     */
    public void indicateIdle() {
        upled.set(false);
        downled.set(false);
    }

    /**
     * Fires everytime a bit changes on the specific chip tied to this elevator.
     * The elevator will then take care of it appropriately and either update
     * the state or the server depending on what event occured.
     *
     * @param o The bit that triggered the update
     * @param b The value that it was set to
     */
    @Override
    public void updateBit(Object o, boolean b) {
        System.out.println("Object " + o + " and Value " + b);
        
        setChanged();
        if (o instanceof ButtonSensor) {
            //An input event happened! Output events we don't really care for, so we ignore them.
            ButtonSensor bs = (ButtonSensor) o;
            BST type = bs.getType();
            switch (type) {
                case TARGETBUTTON:
                    //indicators[bs.getFloor()].set(true);
                    setChanged();
                    notifyObservers(new TargetRequest(shaftNr, bs.getFloor()));
                    break;
                case UPBUTTON:
                    notifyObservers(new CallRequest(shaftNr, bs.getFloor(), CallDirection.UP));
                    break;
                case DOWNBUTTON:
                    notifyObservers(new CallRequest(shaftNr, bs.getFloor(), CallDirection.DOWN));
                    break;
                case OBSTRUCTIONSENSOR:
                    state.obstruct(this);
                    break;
                case FLOORSENSOR:
                    indicators[currentFloor].set(false);
                    currentFloor = bs.getFloor();
                    indicators[currentFloor].set(true);
                    setChanged();
                    notifyObservers(new Event(bs, shaftNr));
                    break;
                case DOORCLOSEDSENSOR:
                    System.out.println("Door close sensor");
                    state.doorClose(this);
                    break;
                case DOOROPENSENSOR:
                    System.out.println("Door open sensor");
                    state.doorOpen(this);
                    setChanged();
                    notifyObservers();
                    break;
                case DOORCLOSEBUTTON:
                    changeState(State.CLOSING_DOORS);
                    break;
                case DOOROPENBUTTON:
                    System.out.println("DOOR OPEN BUTTON PRESSED");
                    changeState(State.OPENING_DOORS);
                    break;
                default:
                    setChanged();
                    notifyObservers(new Event(bs, shaftNr));
            }
        }
    }

    /**
     * Opens the door of the elevator.
     */
    public void openDoor() {
        doorOpen.set(true);
        //short signal is enough to make the door fully open.
        //afterwards bit can be disabled again.
        doorOpen.set(false);
        //for simulation  execute door open state because there is no open sensor.
        if(simulation){
            setChanged();
            notifyObservers("openDoor");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.out.println("interrupted");
            }
            System.out.println("Door open sensor");
            changeState(State.DOORS_OPEN);
        }
    }
    
        /**
     * Closes the door of the elevator.
     */
    public void closeDoor() { 
        doorClose.set(true);
        //short signal is enough to make the door fully close.
        //afterwards bit can be disabled again.
        doorClose.set(false);
        
        //for simulation  execute door open state because there is no open sensor.
        if(simulation){
            setChanged();
            notifyObservers("closeDoor");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.out.println("interrupted");
            }
            System.out.println("Door Close sensor");
            state.doorClose(this);
        }
    }

    /**
     * @param l The label to update with the current state.
     */
    public void giveLabel(JLabel l) {
        label = l;
    }

    /**
     * @return the current floor this elevator is on
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    public void startTimeout() {
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("TIMER!");
                state.idleTimeout(Elevator.this);
            }
        }, 3000);
    }

    
    public void setSimulation(Boolean sim){
        simulation = sim;
    }
}
