package pralka.sim;

import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.Message;
import pralka.msg.PumpControllerMessage;
import pralka.msg.PumpingFinishedMessage;
import pralka.msg.WaterLevelMessage;

public class PumpController extends SimulationThread {

    private static enum State {

        PUMPING_INSIDE,
        PUMPING_OUTSIDE,
        NOT_PUMPING
    }
    private State state;
    private ControlUnit controlUnit;

    @Override
    protected void simulationStep() {
        try {
            Message msg = messageQueue.take();
            if (msg instanceof PumpControllerMessage) {
                switch (((PumpControllerMessage) msg).getActivity()) {
                    case START:
                        if (((PumpControllerMessage) msg).getDirection() == Pump.Direction.INSIDE) {
                            state = State.PUMPING_INSIDE;
                        } else {
                            state = State.PUMPING_OUTSIDE;
                        }
                        break;
                    case STOP:
                        state = State.NOT_PUMPING;
                        break;
                }
            }

            if (msg instanceof WaterLevelMessage) {
                WaterLevelMessage wlMsg = (WaterLevelMessage) msg;
                if ((state == State.PUMPING_INSIDE && wlMsg.isAboveHighLevel()) || (state == State.PUMPING_OUTSIDE && !wlMsg.isAboveLowLevel())) {
                    state = State.NOT_PUMPING;
                    controlUnit.getMessageQueue().put(new PumpingFinishedMessage());
                }
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(PumpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
