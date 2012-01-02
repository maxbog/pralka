package pralka.sim;

import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.Message;
import pralka.msg.MotorControlMessage;

public class Motor extends SimulationThread {

    private void handleMotorControlMessage(MotorControlMessage msg) {
        switch(msg.getActivity()) {
            case START:
                if(state == State.SPINNING) {
                    break;
                }
                direction = msg.getDirection();
                state = Motor.State.SPINNING;
                break;
            case STOP:
                state = State.STOPPED;
                break;                
        }
    }
    
    public static enum Direction {
        LEFT,
        RIGHT
    }
    
    public static enum State {
        SPINNING,
        STOPPED
    }
    
    private int speed;
    private Direction direction;
    private State state;

    @Override
    protected void simulationStep() {
        try {
            Message msg = messageQueue.take();
            if(msg instanceof MotorControlMessage) {
                handleMotorControlMessage((MotorControlMessage)msg);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PumpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
