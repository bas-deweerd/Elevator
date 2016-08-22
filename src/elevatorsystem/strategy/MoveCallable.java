/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevatorsystem.strategy;

import elevatorsystem.elevator.Elevator;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tommie
 */
public class MoveCallable implements Callable<MoveCallable> {
    Elevator e;
    int target;

    public MoveCallable(Elevator e, int target) {
        this.e = e;
        this.target = target;
    }
    
    @Override
    public MoveCallable call() throws Exception {
        Thread t = new Thread() {
            @Override
            public void run() {
                e.goTo(target);
                e.changeState(elevatorsystem.state.State.OPENING_DOORS);
            }
                
        };
        t.start();
        while(t.isAlive()){
            //wait for action to finish
            Thread.sleep(50);
        }
        return this;
    }
}
