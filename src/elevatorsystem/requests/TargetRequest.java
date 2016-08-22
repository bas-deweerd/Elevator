package elevatorsystem.requests;

/**
 * This class was created by timwedde on 19.11.15.
 *
 * Target requests are requests from the inside of the elevator.
 */
public class TargetRequest extends Request {

    public TargetRequest(int shaftNr, int destination) {
        super(shaftNr, destination);
    }
}