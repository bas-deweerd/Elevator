package elevatorsystem.requests;

import elevatorsystem.util.CallDirection;

/**
 * This class was created by timwedde on 19.11.15.
 *
 * Call requests are calls from the outside of the elevator.
 */
public class CallRequest extends Request {

    private CallDirection dir;
    
    public CallRequest(int shaftNr, int destination, CallDirection dir) {
        super(shaftNr, destination);
        this.dir = dir;
    }
}