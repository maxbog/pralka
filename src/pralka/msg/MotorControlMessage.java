package pralka.msg;

import pralka.sim.Motor;

public class MotorControlMessage extends WorkingStateMessage {
    
    Motor.Direction direction;

    public MotorControlMessage(Motor.Direction direction, Activity activity) {
        super(activity);
        this.direction = direction;
    }

    public Motor.Direction getDirection() {
        return direction;
    }
    
    
}
