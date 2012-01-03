package pralka.sim;

import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.ChangeWashingDirectionMessage;
import pralka.msg.Message;
import pralka.msg.MotorControlMessage;
import pralka.msg.WorkingStateMessage;

public class WashingController extends SimulationThread {
    public static final int ONE_DIRECTION_TIME = 20;

    private static enum State {

        WASH_LEFT,
        WASH_RIGHT,
        NOT_WASHING
    }
    private State state;
    private Motor motor;

    public WashingController(Motor motor) {
        this.motor = motor;
    }
    
    

    @Override
    protected void simulationStep() {
        try {
            Message msg = messageQueue.take();
            if (msg instanceof WorkingStateMessage) {
                switch (((WorkingStateMessage) msg).getActivity()) {
                    case START:
                        motor.send(new MotorControlMessage(null, WorkingStateMessage.Activity.STOP));
                        motor.send(new MotorControlMessage(Motor.Direction.LEFT, WorkingStateMessage.Activity.START));
                        state = State.WASH_LEFT;
                        scheduleMessage(new ChangeWashingDirectionMessage(), ONE_DIRECTION_TIME);
                        break;
                    case STOP:
                        clearScheduledMessages();
                        motor.send(new MotorControlMessage(null, WorkingStateMessage.Activity.STOP));
                        state = State.NOT_WASHING;
                        break;
                }
            }
            if (state != State.NOT_WASHING && msg instanceof ChangeWashingDirectionMessage) {
                motor.send(new MotorControlMessage(null, WorkingStateMessage.Activity.STOP));
                if (state == State.WASH_LEFT) {
                    motor.send(new MotorControlMessage(Motor.Direction.RIGHT, WorkingStateMessage.Activity.START));
                    state = State.WASH_RIGHT;
                } else {
                    motor.send(new MotorControlMessage(Motor.Direction.LEFT, WorkingStateMessage.Activity.START));
                    state = State.WASH_LEFT;
                }
                scheduleMessage(new ChangeWashingDirectionMessage(), ONE_DIRECTION_TIME);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PumpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
