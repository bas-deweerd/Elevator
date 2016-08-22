package elevatorsystem.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Sahin
 */
public class Shaft extends JLabel {
    
    private int number;
    private  Cage cage;

    public final static ImageIcon img = new ImageIcon("src/img/shaft.png");

    public Shaft() {
        super(img);  
        super.setBounds(new Rectangle(img.getIconWidth(), img.getIconHeight()));
        configure();
    }

    public void setCage(Cage cage){
        this.cage = cage;
    }
    
    public Cage getCage(){
        return cage;
    }
    
    private void configure() {
        this.setLayout(null);
        setOpaque(false);
    }
    
    public void setNumber(int no){
        this.number = no;
    }
    
    public int getNumber(){
        return number;
    }
}
