package elevatorsystem.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Sahin
 */
public class Cage extends JLabel {

    private final static ImageIcon img = new ImageIcon("src/img/cage.png");
    private final Door door;
    private boolean moving;

    public Cage() {
        super(img);
        super.setBounds(new Rectangle(img.getIconWidth(), img.getIconHeight()));
        door = new Door();
        // super.add(door);
        configure();
    }

    public Door getDoor() {
        return door;
    }

    private void configure() {
      //  setLayout(null);
        setOpaque(false);

    }

    public boolean isMoving() {
        return moving;
    }
       

}
