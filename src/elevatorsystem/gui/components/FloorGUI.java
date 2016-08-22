package elevatorsystem.gui.components;

import elevatorsystem.gui.util.Position;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Sahin
 */
public class FloorGUI extends JPanel {

    private final Buttons bgui;
    private final JPanel requestButtons;
    private Map<String, JButton> callButtons;
    private Circle c1;
    private Circle c2;
    private Circle c3;
    private Circle c4;
    private Circle c5;
    private Circle c6;

    public FloorGUI() {
        super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        super.setBounds(new Rectangle(300, 800));
        super.setVisible(true);
        bgui = new Buttons();
        requestButtons = new JPanel();
        requestButtons.setVisible(true);
        addComponents();
        setRequestButtons();
    }

    /**
     * Creates Floors (with custom made sensors).
     *
     * @return a list of floors for the elevator
     */
    public Map<String, Floor> getFloors() {
        Sensors s = null;
        Map<String, Floor> floors = new LinkedHashMap<>();
        int y = 7;
        int floorY = 32;//y coordinate of the upperleft position of the 3rd floor
        for (int i = 3; i > -1; i--) {
            s = getSensor();
            s.setLocation(100, y);
            Floor f = new Floor(i, new Position(71, floorY), s);
            floors.put("f" + i, f);
            //distance of each floors
            floorY += 158;
            y += 158;
        }
        return floors;
    }

    /**
     * This method creates custom Sensors for each Floor
     *
     * @return
     */
    private Sensors getSensor() {
        c1 = new Circle(8, new Color(223, 255, 83, 255), "down");
        c2 = new Circle(8, new Color(223, 255, 83, 255), "0");
        c3 = new Circle(8, new Color(223, 255, 83, 255), "1");
        c4 = new Circle(8, new Color(223, 255, 83, 255), "2");
        c5 = new Circle(8, new Color(223, 255, 83, 255), "3");
        c6 = new Circle(8, new Color(223, 255, 83, 255), "up");

        Sensors sensors = new Sensors();
        sensors.add("down", c1);
        sensors.add("0", c2);
        sensors.add("1", c3);
        sensors.add("2", c4);
        sensors.add("3", c5);
        sensors.add("up", c6);

        return sensors;
    }

    private void addComponents() {
        for (JButton b : bgui.getButtons().values()) {
            this.add(b);
        }
    }

    /**
     * Adds the same listener,which will handle specific operations, to each
     * Button.
     *
     * @param a
     */
    public void addListener(ActionListener a) {
        for (JButton b : bgui.getButtons().values()) {
            b.addActionListener(a);
        }
//        for(JButton b : callButtons.values())
//            b.addActionListener(a);
    }

    public Map<String, JButton> getButtons() {
        return bgui.getButtons();
    }
    
    public Map<String, JButton> getCallButtons(){
        return callButtons;
    }

    private void setRequestButtons() {
        Icon up = bgui.getButtons().get("up").getIcon();
        Icon down = bgui.getButtons().get("down").getIcon();
        callButtons = new LinkedHashMap<>();
        int i = 0;
        for (Floor f : getFloors().values()) {
            f.getCallUp().setIcon(up);
            f.getCallDown().setIcon(down);
                        
            f.getCallUp().setBorderPainted(false);
            f.getCallUp().setContentAreaFilled(false);
            f.getCallUp().setFocusPainted(false);
            f.getCallDown().setBorderPainted(false);
            f.getCallDown().setContentAreaFilled(false);
            f.getCallDown().setFocusPainted(false);
            
            f.getCallUp().setBounds(new Rectangle(up.getIconWidth(), up.getIconHeight()));
            f.getCallDown().setBounds(new Rectangle(up.getIconWidth(), up.getIconHeight()));
            
            if (f.getFloor() == 0) {
                f.getCallUp().setVisible(false);
                f.getCallUp().setOpaque(false);
            } else if (f.getFloor() == 3) {
                f.getCallDown().setVisible(false);
                f.getCallDown().setOpaque(false);
            }      
            
            requestButtons.add(f.getCallUp());
            requestButtons.add(f.getCallDown());
            
            callButtons.put("up_"+i, f.getCallUp());
            callButtons.put("down_"+i, f.getCallDown());
            i++;
        }
        
        callButtons.get("up_0").setLocation(15, 500);
        callButtons.get("up_1").setLocation(15, 350);
        callButtons.get("up_2").setLocation(15, 175);
        callButtons.get("up_3").setLocation(15, 200);
        
        callButtons.get("down_0").setLocation(15, 550);
        callButtons.get("down_1").setLocation(15, 400);
        callButtons.get("down_2").setLocation(15, 225);
        callButtons.get("down_3").setLocation(15, 75);
    }

    public JPanel getRequestButtons() {
        return requestButtons;
    }
}
