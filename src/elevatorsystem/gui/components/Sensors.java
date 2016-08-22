package elevatorsystem.gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This Class creates sensors. 
 * It enables you to add new Sensors to a label as well as determine the distance
 * and change the color to (de)activate a sensor
 * @author Sahin
 */
public class Sensors extends JLabel {

    private final Map<String, Circle> map = new LinkedHashMap<>();
    private int distance;

    public Sensors() {
        super.setLayout(null);
        super.setOpaque(false);
//        super.setBackground(new Color(0,0,0,0));
        super.setBounds(new Rectangle(150, 20));
        distance = 20;
    }

    public void add(String desc, Circle c) {
        map.put(desc, c);
    }
    
    public Map<String, Circle> getMap(){
        return map;
    }
    
    public void setDistance(int dist){
        this.distance = dist;
    }
    
    /**
     * the distance of each sensor
     * @return 
     */
    public int getDistance(){
        return distance;
    }

    /**
     * Changes the sensor in order to activate or deactivate
     * @param led
     * @param c 
     */
    public void change(String led, Color c) {
        if (map.containsKey(led)) {
            map.get(led).setColor(c);     
        }
    }
    
    /**
     * Retrieves the color of a specific Sensor.
     * @param led string representation of a sensor
     * @return 
     */
    public Color getColor(String led){
        if(map.containsKey(led)){
            return map.get(led).getColor();
        } else return null;
    }

    /**
     * Draws Circles to represent Sensors for each floor
     * @param g 
     */
    private void draw(Graphics g) {
        Graphics2D ga = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        ga.setRenderingHints(rh);
        int x = 0, y = 0;
        for (Circle c : map.values()) {
            ga.setPaint(c.getColor());
            ga.fillOval(x, y, c.getD(), c.getD());
            x += distance;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }
}
