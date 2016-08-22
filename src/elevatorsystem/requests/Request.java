package elevatorsystem.requests;

/**
 * This class was created by timwedde on 19.11.15.
 * <p>
 * This is the abstract parent that other requests inherit from.
 * It handles saving the time of the call and specifies needed operations.
 */
public abstract class Request {

    private int shaftNr;
    private int destination;
    private long time;

    public Request(int shaftNr, int destination) {
        this.shaftNr = shaftNr;
        this.destination = destination;
        this.time = System.currentTimeMillis();
    }

    public int getShaftNr() {
        return shaftNr;
    }

    public int getDestination() {
        return destination;
    }

    public long getTime() {
        return time;
    }
}