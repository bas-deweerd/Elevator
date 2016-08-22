package elevatorsystem.gui.util;

import elevatorsystem.gui.components.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.Timer;

/**
 *
 * @author merve
 */
public class FloorListener implements ActionListener {

    private final Timer timer;
    private final Cage cage;
    
    private boolean moveUp;
    public static boolean stopped;
    private static boolean isRunning = false;
    
    private final List<Floor> floorList;
        
    private final SensorController sensorController;
    private final DoorController doorController;
    
    private String previousFloor;
    private int y;
    
    public FloorListener(Cage cage, Map<String, Floor> floors) {
        timer = new Timer(15, this);
        this.cage = cage;
        sensorController = new SensorController();
        floorList = new ArrayList<>(floors.values());
        doorController = new DoorController();
    }

    public synchronized void start() {
        isRunning = true;
        timer.start();
    }

    public synchronized void stop() {
        timer.stop();
        stopped = true;
    }

    /**
     * If the cage reaches the y coordinate, it stops.
     *
     * @param b
     * @param y
     */
    public void setMoveUp(boolean b, int y) {       
        setMoveUp(b);
        this.y = y;
    }

    public void setMoveUp(boolean b) {
        moveUp = b;
        timer.setInitialDelay(1200);
        doorController.close(cage);
        
    }
    
    public void open(){
        if(!timer.isRunning())
        doorController.open(cage);
    }
    
    public void close(){
        if(!timer.isRunning())
        doorController.close(cage);
    }
    
    private String getSensorOnFloor() {
        String sensor = null;
        for (Floor f : floorList) {
            if (y == f.getUpperLeft().getY()) {
                sensor = "" + f.getFloor();
            }
        }
        return sensor;
    }

    public Cage getCage() {
        return cage;
    }

    public boolean isMoving() {
        return isRunning;
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        if (stopped) {//stopped manually
            timer.stop();
            //doorController.open(cage);
            sensorController.activateOne(getSensorOnFloor(), floorList);
            isRunning = false;
            return;

        } else if (cage.getLocation().y == 31) {//reached last floor
            timer.stop();
            //doorController.open(cage);
            sensorController.activateOne("3", floorList);
            cage.setLocation(cage.getX(), cage.getY() + 1);
            isRunning = false;
            return;

        } else if (cage.getLocation().y == 507) {//reached first floor
            timer.stop();
            //doorController.open(cage);
            sensorController.activateOne("0", floorList);
            cage.setLocation(cage.getX(), cage.getY() - 1);
            isRunning = false;
            return;

        }
        if (cage.getY() == y) {
            timer.stop();
            //doorController.open(cage);
            sensorController.activateOne(getSensorOnFloor(), floorList);
            isRunning = false;
            return;
        }
        if (moveUp) {
            cage.setLocation(cage.getX(), cage.getY() - 1);
            isRunning = true;
            sensorController.deactivate(previousFloor, floorList);
            for (Floor f : floorList) {
                if (cage.getLocation().y >= f.getUpperLeft().getY()) {
                    sensorController.activateTwo("" + f.getFloor(), "up", floorList);
                }
            }
            
        } else {
            cage.setLocation(cage.getLocation().x, cage.getY() + 1);
            isRunning = true;
            sensorController.deactivate(previousFloor, floorList);
            for (Floor f : floorList) {
                if (cage.getY() >= f.getUpperLeft().getY()) {
                    sensorController.activateTwo("" + f.getFloor(), "down", floorList);
                }
            }
        }
        previousFloor = getSensorOnFloor();
    }

    

}
