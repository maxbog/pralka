package pralka.sim;

import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.ChangeWashingDirectionMessage;
import pralka.msg.Message;
import pralka.msg.MotorControlMessage;
import pralka.msg.WashingControllerMessage;
import pralka.msg.WorkingStateMessage;

public class WashingController extends SimulationThread {
    public static final int ONE_DIRECTION_TIME = 20;
    public static final int CRADLE_ONE_DIRECTION_TIME = 1;

    private static enum State {

        WASH_LEFT,
        WASH_RIGHT,
        NOT_WASHING
    }
    private State state;
    private Motor motor;
    private boolean cradleMoves;

    public WashingController(Motor motor) {
        this.motor = motor;
    }

    @Override
    protected void simulationStep() {
        try {
            Message msg = messageQueue.take();
            if (msg instanceof WashingControllerMessage) {
                switch (((WashingControllerMessage) msg).getActivity()) {
                    case START:
                        cradleMoves = ((WashingControllerMessage) msg).doCradleMoves();
                        motor.send(new MotorControlMessage(null, WorkingStateMessage.Activity.STOP));
                        motor.send(new MotorControlMessage(Motor.Direction.LEFT, WorkingStateMessage.Activity.START));
                        state = State.WASH_LEFT;
                        scheduleMessage(new ChangeWashingDirectionMessage(), cradleMoves ? CRADLE_ONE_DIRECTION_TIME : ONE_DIRECTION_TIME);
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
                scheduleMessage(new ChangeWashingDirectionMessage(), cradleMoves ? CRADLE_ONE_DIRECTION_TIME : ONE_DIRECTION_TIME);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PumpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
