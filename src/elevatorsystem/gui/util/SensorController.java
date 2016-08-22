package elevatorsystem.gui.util;

import elevatorsystem.gui.components.*;
import java.awt.Color;
import java.util.List;

/**
 *
 * @author merve
 */
public class SensorController {

    private final static Color defaultColor = new Color(223,255,83,255);
    
    
    public SensorController(){
    }
    
    /**
     * Activates given sensor on each floor.
     * @param sensor
     * @param floors 
     */
    public synchronized void activate(String sensor, List<Floor> floors){
        for(Floor f : floors){         
            activate(f, sensor);
        }
    }
    
    /**
     * Deactivates given sensor on each floor.
     * @param sensor
     * @param floors 
     */
    public synchronized void deactivate(String sensor, List<Floor>  floors){
        for(Floor f : floors){
            deactivate(f, sensor);
        }
    }
    
    public void deactivateFloors(List<Floor> floors){
        for(Floor f : floors){
            deactivate(""+f.getFloor(), floors);
        }
    }
    
    public void activateFloors(List<Floor> floors){
        for(Floor f : floors){
            activate(""+f.getFloor(), floors);
        }
    }
    
    /**
     * Activates a single sensor.
     * @param floor
     * @param sensor 
     */
    public synchronized void activate(Floor floor, String sensor){
        floor.getSensors().change(sensor, Color.red); 
        floor.getSensors().repaint();
    }
    
    /**
     * Deactivates a single sensor.
     * @param floor
     * @param sensor 
     */
    public synchronized void deactivate(Floor floor, String sensor){
        floor.getSensors().change(sensor, defaultColor); 
        floor.getSensors().repaint();
    }
    
    /**
     * Deactivates all sensors, except for the one given.
     * @param sensor to be activated
     * @param floors 
     */
    public synchronized void activateOne(String sensor, List<Floor> floors){
        deactivateAll(floors);
        activate(sensor, floors);
    }
    
    public synchronized void deactivateAll(List<Floor> floors){
        for(Floor f : floors){
            for(Circle c : f.getSensors().getMap().values()){
                deactivate(f, c.getName());
            }
        }
    }
    
    /**
     * Deactivates all sensors except for the two ones given in the parameters.
     * @param sensor
     * @param sensor2
     * @param floors 
     */
    public synchronized void activateTwo(String sensor, String sensor2, List<Floor> floors){
        deactivateAll(floors);
        activate(sensor, floors);
        activate(sensor2, floors);
    }
    
    
    /**
     * Checks if a specific sensor is activated.
     * @param floor
     * @param sensor
     * @return 
     */
    public boolean isActivated(Floor floor, String sensor){
        return floor.getSensors().getColor(sensor) != defaultColor;
    }
    
}
