package pralka.sim;

import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.DoorControlMessage;
import pralka.msg.Message;

public class Door extends SimulationThread {

    private void handleDoorControlMessage(DoorControlMessage msg) {
        switch (msg.getActivity()) {
            case OPEN:
                if (lockState == LockState.UNLOCKED) {
                    state = State.OPENED;
                }
                break;
            case CLOSE:
                state = State.CLOSED;
                break;
            case LOCK:
                lockState = LockState.LOCKED;
                break;
            case UNLOCK:
                lockState = LockState.UNLOCKED;
                break;
        }
    }

    public static enum LockState {
        LOCKED,
        UNLOCKED
    }

    public static enum State {
        OPENED,
        CLOSED
    }
    
    private State state = State.CLOSED;
    private LockState lockState = LockState.UNLOCKED;

    public boolean isOpen() {
        return (state == State.OPENED);
    }

    public LockState getLockState() {
        return lockState;
    }

    public State getDoorState() {
        return state;
    }
    
    

    @Override
    protected void simulationStep() {
        try {
            Message msg = messageQueue.take();
            if (msg instanceof DoorControlMessage) {
                handleDoorControlMessage((DoorControlMessage) msg);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PumpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
