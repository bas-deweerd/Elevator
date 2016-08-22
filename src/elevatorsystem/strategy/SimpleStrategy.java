package elevatorsystem.strategy;

import elevatorsystem.elevator.Elevator;
import elevatorsystem.event.Event;
import elevatorsystem.io.BST;
import elevatorsystem.requests.CallRequest;
import elevatorsystem.requests.Request;
import elevatorsystem.requests.TargetRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class was created by timwedde on 19.11.15.
 * <p>
 * This strategy queues all incoming requests and handles them one after another.
 */
public class SimpleStrategy extends Strategy {

    private ArrayList<Elevator> elevatorList = new ArrayList<>();
    private Queue<Request> list = new LinkedBlockingQueue<>();
//private Queue<Request> list = new PriorityBlockingQueue<>(10, (Comparator<Request>) (o1, o2) -> Long.compare(o1.getTime(), o2.getTime()));
    private Integer[] floors;

    @Override
    public void addTargetRequest(TargetRequest r) {
        // Step 1: Normal behaviour
        super.addTargetRequest(r);
        // Step 2: Strategy specific behaviour:
        for (Request r1 : list) if (r.getDestination() == r1.getDestination()) return;
        list.add(r);
    }

    @Override
    public void addCallRequest(CallRequest r) {
        // Step 1: Normal behaviour:
        super.addCallRequest(r);
        // Step 2: Strategy specific behaviour:
        list.add(r); // Does not check for duplicate requests.
        System.out.println("ADDED CALL REQUEST!");
    }

    @Override
    public void triggerEvent(Event e) {
//        if (e.getEventType() == BST.FLOORSENSOR) {
//            if (floors != null) floors[e.getShaftNr()] = e.getPayload().getFloor();
//        }
    }

    @Override
    public void setElevators(ArrayList<Elevator> elevators) {
        this.elevatorList = elevators;
        floors = new Integer[elevators.size()];
        Arrays.fill(floors, 0);
        start();
    }

    /**
     * Starts a new thread that polls the queue, handling requests if available.
     */
    public void start() {
        System.out.println("STARTED THREAD!");
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    Request cr = list.poll();
                    if (cr != null) {
                        for (Elevator e : elevatorList) {
                            if (e.getShaftNr() == cr.getShaftNr()) {
                                e.goTo(cr.getDestination());
                                while (e.getCurrentFloor() != cr.getDestination() || e.getState() == elevatorsystem.state.State.MOVING) {
                                    try {
                                        Thread.sleep(5);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

}