/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevatorsystem.strategy;

import elevatorsystem.elevator.Elevator;
import static elevatorsystem.state.State.OPENING_DOORS;
import static java.lang.Thread.sleep;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bas
 */
public class FPNCallable implements Callable{
    int target;
    Elevator elevator;
    
    public FPNCallable(Elevator elevator, int target){
        this.target=target;
        this.elevator=elevator;
    }
    
    @Override
    public Object call() throws Exception {
        Thread t=new Thread(){
            @Override
            public void run(){
                elevator.goTo(target);
                elevator.changeState(elevatorsystem.state.State.IDLE);
            }
        };
        t.start();
        while(t.isAlive()){
            Thread.sleep(50);
        }
        return this;
    }
}
