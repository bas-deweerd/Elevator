/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevatorsystem.strategy;

import elevatorsystem.elevator.Elevator;
import elevatorsystem.event.Event;
import elevatorsystem.io.BST;
import elevatorsystem.requests.TargetRequest;
import elevatorsystem.state.State;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tommie
 */
public class SimpleStrategy2 extends Strategy {

    private final ExecutorService ex = Executors.newCachedThreadPool();
    private final ExecutorCompletionService<MoveCallable> ex2 = new ExecutorCompletionService(ex);
    private LinkedBlockingDeque<MoveCallable> queue = new LinkedBlockingDeque<>();

    @Override
    public void triggerEvent(Event e) {
        BST type = e.getEventType();
        switch (type) {
            case TARGETBUTTON: {
                processNextRequest();
                break;
            }
            case FLOORSENSOR: {
                System.out.println("Floor " + e.getPayload().getFloor() + " reached.");
                break;
            }
        }
    }

    public void start() {

        Thread t = new Thread() {
            @Override
            public void run() {

                while (true) {
                    try {
                        MoveCallable ht = queue.take();

                        Future fut = ex2.submit(ht);
                        
                        fut.get();


                        System.out.println("future was completed");
                        Thread.sleep(5000);

                    //e.changeState(elevatorsystem.state.State.OPENING_DOORS);
                    } catch (Exception ex) {
                        //
                    }
                }
            }
        };

        t.start();
    }

    private void processNextRequest() {
        try {
            TargetRequest req = targetRequests.take();
            Elevator e = elevatorList.get(0);

            queue.add(new MoveCallable(e, req.getDestination()));

        } catch (InterruptedException ex) {
            Logger.getLogger(SimpleStrategy2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setElevators(ArrayList<Elevator> elevators) {
        super.setElevators(elevators);

        start();
    }

}
