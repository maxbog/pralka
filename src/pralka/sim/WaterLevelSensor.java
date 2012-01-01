package pralka.sim;

import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.GetMeasurementMessage;
import pralka.msg.Message;
import pralka.msg.WaterLevelMessage;

public class WaterLevelSensor extends SimulationThread {

    private static final int LOW_LEVEL = 0;
    private static final int HIGH_LEVEL = 10;
    private Environment environment;

    public WaterLevelSensor(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void simulationStep() {
        try {
            Message msg = messageQueue.take();
            if(msg instanceof GetMeasurementMessage) {
                ((GetMeasurementMessage)msg).getReturnThread().getMessageQueue().put(new WaterLevelMessage(environment.getWaterLevel() >= HIGH_LEVEL, environment.getWaterLevel() >= LOW_LEVEL));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(TemperatureSensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
