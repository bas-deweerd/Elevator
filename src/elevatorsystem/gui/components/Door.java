package elevatorsystem.gui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Sahin
 */
public class Door implements ActionListener {

    private boolean isOpen = false;
    private boolean obstructed = false;
    private final Sensors sensors;
    private final Timer timer;
    private int n = 3;
    private Circle c1;
    private Circle c2;
    private Circle c3;

    public Door() {
        sensors = new Sensors();
        isOpen = true;
        obstructed = false;
        timer = new Timer(300, this);
    }

    public Sensors getSensors() {
        c1 = new Circle(8, new Color(223, 255, 83, 255), "1");
        c2 = new Circle(8, new Color(223, 255, 83, 255), "2");
        c3 = new Circle(8, new Color(223, 255, 83, 255), "3");
        sensors.add("1", c1);
        sensors.add("2", c2);
        sensors.add("3", c3);
        sensors.setDistance(20);
        return sensors;
    }

    public boolean IsOpen() {
        return isOpen;
    }

    public boolean isObstructed() {
        return obstructed;
    }

    public void setObstructed() {
        this.obstructed = true;
    }

    private synchronized void close(String sensor) {
        sensors.getMap().get(sensor).setColor(Color.red);
        sensors.repaint();
    }

    public void close() {
        isOpen = false;
    }

    public void open() {
        isOpen = true;
    }

    private synchronized void open(String sensor) {
        sensors.getMap().get(sensor).setColor(new Color(223, 255, 83, 255));
        sensors.repaint();
    }

    public synchronized void start() {
        timer.start();
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        if (isOpen || obstructed) {
            if (n > 0) {
                open("" + n);
                n--;
            } else {
                obstructed = false;               
                timer.stop();
                n = 3;
            }
        } else {
            if (n > 0) {
                close("" + n);
                n--;
            } else {
                timer.stop();
                n = 3;
            }
        }

    }

}
