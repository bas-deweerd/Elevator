package elevatorsystem.gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Sahin
 */
public class Buttons {

    private JButton up;
    private JButton down;
    private JButton obstruct;
    private JButton f0;
    private JButton f1;
    private JButton f2;
    private JButton f3;
    private JButton stop;
    private JButton close;
    private JButton open;
    private JButton floorSensor;

    private Map<String, JButton> buttons;

    public Buttons() {
        init();
    }

    private void init() {

        f0 = new JButton(new ImageIcon("src/img/Buttons/Inside_elevator/0.png"));
        f1 = new JButton(new ImageIcon("src/img/Buttons/Inside_elevator/1.png"));
        f2 = new JButton(new ImageIcon("src/img/Buttons/Inside_elevator/2.png"));
        f3 = new JButton(new ImageIcon("src/img/Buttons/Inside_elevator/3.png"));
        
        Image upIcon = new ImageIcon("src/img/Buttons/Floors/Light_Up_Neutral.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        Image downIcon = new ImageIcon("src/img/Buttons/Floors/Light_Down_Neutral.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        Image obstructIcon = new ImageIcon("src/img/Buttons/Inside_elevator/OBS.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT);
        Image alarmIcon = new ImageIcon("src/img/Buttons/Inside_elevator/Alarm.png").getImage().getScaledInstance(40,40, Image.SCALE_DEFAULT);
        Image openIcon = new ImageIcon("src/img/Buttons/Inside_elevator/Door_Open.png").getImage().getScaledInstance(40,40, Image.SCALE_DEFAULT);
        Image closeIcon = new ImageIcon("src/img/Buttons/Inside_elevator/Door_Closed.png").getImage().getScaledInstance(40,40, Image.SCALE_DEFAULT);
        
        
        up = new JButton(new ImageIcon(upIcon));
        down = new JButton(new ImageIcon(downIcon));
        obstruct = new JButton(new ImageIcon(obstructIcon));

        stop = new JButton(new ImageIcon(alarmIcon));
        open = new JButton(new ImageIcon(openIcon));
        close = new JButton(new ImageIcon(closeIcon));
        floorSensor = new JButton("floor sensor");
        
        
        buttons = new LinkedHashMap<>();
        buttons.put("f0", f0);
        buttons.put("f1", f1);
        buttons.put("f2", f2);
        buttons.put("f3", f3);
        buttons.put("obstruct", obstruct);
        buttons.put("stop", stop);
        buttons.put("up", up);
        buttons.put("down", down);
        buttons.put("open", open);
        buttons.put("close", close);
        

        for (JButton butt : buttons.values()) {
            butt.setBorderPainted(false);
            butt.setContentAreaFilled(false);
            butt.setFocusPainted(false);
            butt.setOpaque(false);
        }
        buttons.put("floor_sensor", floorSensor);

    }

    public Map<String, JButton> getButtons() {
        return buttons;
    }

}
