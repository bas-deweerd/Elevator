package elevatorsystem.gui.util;

import elevatorsystem.gui.components.Cage;
import elevatorsystem.gui.components.Floor;
import elevatorsystem.requests.TargetRequest;
import elevatorsystem.state.State;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author merve
 */
public class GuiController implements ElevatorOperation{

     private FloorListener listener;
    private Floor initalFloor;
    private final Map<String, Floor> floors;
    private final DoorController doorController;

    public GuiController(Map<String, Floor> floors) {
        doorController = new DoorController();
        this.floors = floors;
    }

    @Override
    public void close(Cage cage) {
        listener = new FloorListener(cage, floors);
        if(listener != null){
            listener.close();
        }
    }

    @Override
    public void open(Cage cage) {
        listener = new FloorListener(cage, floors);
        if(listener != null){
            listener.open();
        }
    }

    @Override
    public void obstruct() {
        open(listener.getCage());
    }

    @Override
    public synchronized void moveUp(Cage cage) {
        FloorListener.stopped = false;
        listener = new FloorListener(cage, floors);
        listener.setMoveUp(true);
        listener.start();
    }

    @Override
    public synchronized void moveDown(Cage cage) {
        FloorListener.stopped = false;
        listener = new FloorListener(cage, floors);
        listener.setMoveUp(false);
        listener.start();
    }

    @Override
    public synchronized void moveTo(Cage cage, Floor floor) {

        listener = new FloorListener(cage, floors);
        FloorListener.stopped = false;
        try {
            if (floor.getUpperLeft().getY() != getCurrentFloor(cage).getUpperLeft().getY()) {
                if (floor.getUpperLeft().getY() > getCurrentFloor(cage).getUpperLeft().getY()) {
                    listener.setMoveUp(false, floor.getUpperLeft().getY());
                    listener.start();
                    
                    

                } else if (floor.getUpperLeft().getY() < getCurrentFloor(cage).getUpperLeft().getY()) {
                    listener.setMoveUp(true, floor.getUpperLeft().getY());
                    listener.start();
                }
            }
        } catch (Throwable t) {

        }
        
    }

    @Override
    public Floor getCurrentFloor(Cage cage) {
        Floor f = null;
        for (Floor fl : floors.values()) {
            if (cage.getY() == fl.getUpperLeft().getY()) {
                f = fl;
            }
        }
        return f;
    }

    public Floor getInitialFloor() {
        return initalFloor;
    }

    @Override
    public void shutDown(Cage cage) {
        moveTo(cage, floors.get("f0"));
        doorController.close(cage);
    }

    @Override
    public void call(Cage cage, Floor floor) {
        //TODO
    }

    public void call(Floor floor, boolean up) {
        //TODO
    }


    public void stop() {
        FloorListener.stopped = true;
    }

    public boolean isMoving() {
        return listener.isMoving();
    }

    @Override
    public void stop(Cage cage) {
   
    }



}
