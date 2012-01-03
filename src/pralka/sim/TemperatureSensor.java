package pralka.sim;

import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.GetMeasurementMessage;
import pralka.msg.Message;
import pralka.msg.TemperatureMessage;

public class TemperatureSensor extends SimulationThread {

    private Environment environment;

    public TemperatureSensor(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void simulationStep() {
        try {
            Message msg = messageQueue.take();
            if(msg instanceof GetMeasurementMessage) {
                ((GetMeasurementMessage)msg).getReturnThread().send(new TemperatureMessage(environment.getWaterTemperature()));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(TemperatureSensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
