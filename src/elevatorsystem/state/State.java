package elevatorsystem.state;

import elevatorsystem.elevator.Elevator;

/**
 * This class was created by timwedde on 19.11.15.
 *
 * handles state behaviour for each elevator.
 */
public enum State implements ElevatorEvents {

    START_UP {
        @Override
        public void entry(Elevator context) {
            System.out.println("STARTUP STATE");
            context.closeDoor();
            if(context.getCurrentFloor() != 0){
                context.goTo(0);
                context.changeState(OPENING_DOORS);
            }
        }
    },
    IDLE {
        @Override
        public void entry(Elevator context){
            System.out.println("IDLE STATE");
        }
        
        @Override
        public void idleTimeout(Elevator context) {
            System.out.println("IDLE TIMEOUT");
        }
        
        @Override
        public void doorOpen(Elevator context){
            System.out.println("==============================");
            context.changeState(OPENING_DOORS);
        }
        
    },
    MOVING {

    },
    OPENING_DOORS {
        @Override
        public void entry(Elevator context) {
            System.out.println("OPENING DOORS");
            context.openDoor();
        }

        @Override
        public void doorOpen(Elevator context) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            context.changeState(DOORS_OPEN);
        }
    },
    DOORS_OPEN {
        @Override
        public void entry(Elevator context) {
            context.startTimeout();
        }

        @Override
        public void idleTimeout(Elevator context) {
            context.changeState(CLOSING_DOORS);
        }
    },
    CLOSING_DOORS {
        @Override
        public void entry(Elevator context) {
            System.out.println("CLOSING DOOR STATE");
            context.closeDoor();
        }

        @Override
        public void doorClose(Elevator context) {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            context.changeState(IDLE);
        }
    },
// Could not be used because there's no obstruction sensor or alarm    
//    OBSTRUCTED {
//        @Override
//        public void entry(Elevator context) {
//            System.out.println("OBSTRUCTED STATE");
//            context.openDoor();
//        }
//
//        @Override
//        public void obstruct(Elevator context) {
//            context.changeState(OBSTRUCTED_WAITING);
//        }
//    },
//    OBSTRUCTED_WAITING {
//        @Override
//        public void idleTimeout(Elevator context) {
//            System.out.println("OBSTRUCTED WAITING");
//            context.changeState(CLOSING_DOORS);
//        }
//    },
//    ALARM {
//
//    },
    SHUTDOWN {
        // TODO
    };
    //SUPER STATE

    @Override
    public void entry(Elevator context) {

    }

    @Override
    public void exit(Elevator context) {

    }

    @Override
    public void idleTimeout(Elevator context) {

    }

    @Override
    public void obstruct(Elevator context) {

    }

    @Override
    public void doorClose(Elevator context) {
        
    }

    @Override
    public void doorOpen(Elevator context) {
        context.openDoor();
    }
}
