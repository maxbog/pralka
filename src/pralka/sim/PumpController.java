package pralka.sim;

import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.GetMeasurementMessage;
import pralka.msg.Message;
import pralka.msg.PumpControlMessage;
import pralka.msg.PumpControllerMessage;
import pralka.msg.PumpingFinishedMessage;
import pralka.msg.WaterLevelMessage;
import pralka.msg.WorkingStateMessage.Activity;

public class PumpController extends SimulationThread {

    private static enum State {

        PUMPING_INSIDE,
        PUMPING_OUTSIDE,
        NOT_PUMPING
    }
    private State state;
    private ControlUnit controlUnit;
    private WaterLevelSensor waterLevelSensor;
    private Pump pump;

    public PumpController(ControlUnit controlUnit, WaterLevelSensor waterLevelSensor, Pump pump) {
        this.controlUnit = controlUnit;
        this.waterLevelSensor = waterLevelSensor;
        this.pump = pump;
    }

    @Override
    protected void simulationStep() {
        try {
            Message msg = messageQueue.take();
            if (msg instanceof PumpControllerMessage) {
                switch (((PumpControllerMessage) msg).getActivity()) {
                    case START:
                        if (((PumpControllerMessage) msg).getDirection() == Pump.Direction.INSIDE) {
                            state = State.PUMPING_INSIDE;
                            pump.getMessageQueue().put(new PumpControlMessage(Activity.START, Pump.Direction.INSIDE));
                        } else {
                            state = State.PUMPING_OUTSIDE;
                            pump.getMessageQueue().put(new PumpControlMessage(Activity.START, Pump.Direction.OUTSIDE));
                        }
                        scheduleMessage(new GetMeasurementMessage(this), waterLevelSensor, 0.1);
                        break;
                    case STOP:
                        state = State.NOT_PUMPING;
                        pump.getMessageQueue().put(new PumpControlMessage(Activity.STOP, null));
                        break;
                }
            }

            if (msg instanceof WaterLevelMessage) {
                WaterLevelMessage wlMsg = (WaterLevelMessage) msg;
                if ((state == State.PUMPING_INSIDE && wlMsg.isAboveHighLevel()) || (state == State.PUMPING_OUTSIDE && !wlMsg.isAboveLowLevel())) {
                    state = State.NOT_PUMPING;
                    pump.getMessageQueue().put(new PumpControlMessage(Activity.STOP, null));
                    controlUnit.getMessageQueue().put(new PumpingFinishedMessage());
                }
                scheduleMessage(new GetMeasurementMessage(this), waterLevelSensor, .1);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(PumpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
