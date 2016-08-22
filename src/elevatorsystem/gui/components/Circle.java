package elevatorsystem.gui.components;

import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author Sahin
 */
public class Circle  extends JLabel{

    private Color color;
    private final int d;
    private final String name;
    
    public Circle(int d, String name){
        this.d = d;
        this.name = name;
    }
    
    public Circle(int d, Color c, String name) {
        this.d = d;
        this.color = c;
        this.name = name;
    }    

    public int getD() {
        return d;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    @Override
    public String getName(){
        return name;
    }
}
