package pralka.msg;

import pralka.sim.Pump;
import pralka.sim.Pump.Direction;

public class PumpControlMessage extends WorkingStateMessage {
    Pump.Direction direction;


    public Direction getDirection() {
        return direction;
    }

    public PumpControlMessage(Activity activity, Direction direction) {
        super(activity);
        this.direction = direction;
    }
    
    
}
