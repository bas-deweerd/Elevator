package elevatorsystem.event;

import elevatorsystem.io.BST;
import elevatorsystem.io.ButtonSensor;

/**
 * This class was created by timwedde on 19.11.15.
 * <p>
 * Wraps all other events that are not call or target requests.
 */
public class Event {

    private final int shaftNr;
    private ButtonSensor payload;

    public Event(ButtonSensor payload, int shaftNr) {
        this.payload = payload;
        this.shaftNr = shaftNr;
    }

    public BST getEventType() {
        return payload.getType();
    }

    public ButtonSensor getPayload() {
        return payload;
    }

    public int getShaftNr() {
        return shaftNr;
    }
}