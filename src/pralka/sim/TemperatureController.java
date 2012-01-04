package pralka.sim;

import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.GetMeasurementMessage;
import pralka.msg.WorkingStateMessage;
import pralka.msg.Message;
import pralka.msg.TemperatureControllerMessage;
import pralka.msg.TemperatureMessage;

public class TemperatureController extends SimulationThread {
    private static final double MEASUREMENT_PERIOD = 0.1;

    private final double EPSILON = 2.;

    private static enum HeatingState {

        HEATER_ON,
        HEATER_OFF
    }

    private static enum State {
        ON,
        OFF
    }
    private HeatingState heatingState = HeatingState.HEATER_OFF;
    private State state;
    private TemperatureSensor temperatureSensor;
    private Heater heater;
    private double setTemperature;

    public TemperatureController(TemperatureSensor temperatureSensor, Heater heater) {
        this.temperatureSensor = temperatureSensor;
        this.heater = heater;
    }
    
    

    @Override
    protected void simulationStep() {
        try {
            Message msg = messageQueue.take();
            if (msg instanceof TemperatureControllerMessage) {
                switch (((TemperatureControllerMessage) msg).getActivity()) {
                    case START:
                        setTemperature = ((TemperatureControllerMessage) msg).getTargetTemperature();
                        temperatureSensor.scheduleMessage(new GetMeasurementMessage(this), MEASUREMENT_PERIOD);
                        state = State.ON;
                        break;
                    case STOP:
                        clearScheduledMessages();
                        heater.send(new WorkingStateMessage(WorkingStateMessage.Activity.STOP));
                        heatingState = HeatingState.HEATER_OFF;
                        state = State.OFF;
                        break;
                }
            }

            if (state == State.ON && msg instanceof TemperatureMessage) {
                double currentTemp = ((TemperatureMessage) msg).getMeasurement();
                switch (heatingState) {
                    case HEATER_ON:
                        if (setTemperature + EPSILON < currentTemp) {
                            heatingState = HeatingState.HEATER_OFF;
                            heater.send(new WorkingStateMessage(WorkingStateMessage.Activity.STOP));
                        }
                        break;
                    case HEATER_OFF:
                        if (setTemperature - EPSILON > currentTemp) {
                            heatingState = HeatingState.HEATER_ON;
                            heater.send(new WorkingStateMessage(WorkingStateMessage.Activity.START));
                        }
                        break;
                }
                temperatureSensor.scheduleMessage(new GetMeasurementMessage(this), MEASUREMENT_PERIOD);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(TemperatureController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
