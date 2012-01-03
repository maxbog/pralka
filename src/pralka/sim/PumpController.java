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
    private static final double MEASUREMENT_PERIOD = 0.1;

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
                            pump.send(new PumpControlMessage(Activity.STOP, null));
                            pump.send(new PumpControlMessage(Activity.START, Pump.Direction.INSIDE));
                        } else {
                            state = State.PUMPING_OUTSIDE;
                            pump.send(new PumpControlMessage(Activity.STOP, null));
                            pump.send(new PumpControlMessage(Activity.START, Pump.Direction.OUTSIDE));
                        }
                        waterLevelSensor.scheduleMessage(new GetMeasurementMessage(this), MEASUREMENT_PERIOD);
                        break;
                    case STOP:
                        clearScheduledMessages();
                        state = State.NOT_PUMPING;
                        pump.send(new PumpControlMessage(Activity.STOP, null));
                        break;
                }
            }

            if (msg instanceof WaterLevelMessage) {
                WaterLevelMessage wlMsg = (WaterLevelMessage) msg;
                if ((state == State.PUMPING_INSIDE && wlMsg.isAboveHighLevel()) || (state == State.PUMPING_OUTSIDE && !wlMsg.isAboveLowLevel())) {
                    state = State.NOT_PUMPING;
                    pump.send(new PumpControlMessage(Activity.STOP, null));
                    controlUnit.send(new PumpingFinishedMessage());
                }
                waterLevelSensor.scheduleMessage(new GetMeasurementMessage(this), MEASUREMENT_PERIOD);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(PumpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
