package pralka.sim;

import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.GetMeasurementMessage;
import pralka.msg.WorkingStateMessage;
import pralka.msg.Message;
import pralka.msg.TemperatureControllerMessage;
import pralka.msg.TemperatureMessage;

public class TemperatureController extends SimulationThread {

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
                        scheduleMessage(new GetMeasurementMessage(this), temperatureSensor, 1);
                        state = State.ON;
                        break;
                    case STOP:
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
                            heater.getMessageQueue().put(new WorkingStateMessage(WorkingStateMessage.Activity.STOP));
                        }
                        break;
                    case HEATER_OFF:
                        if (setTemperature - EPSILON > currentTemp) {
                            heatingState = HeatingState.HEATER_ON;
                            heater.getMessageQueue().put(new WorkingStateMessage(WorkingStateMessage.Activity.START));
                        }
                        break;
                }
                scheduleMessage(new GetMeasurementMessage(this), temperatureSensor, 1);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(TemperatureController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
