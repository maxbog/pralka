package pralka.msg;

import pralka.sim.Pump;
import pralka.sim.Pump.Direction;

public class PumpControllerMessage extends WorkingStateMessage {
    Pump.Direction direction;

    public PumpControllerMessage(Direction direction, Activity activity) {
        super(activity);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
    
    
}
