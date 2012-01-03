package pralka.sim;

import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.WorkingStateMessage;
import pralka.msg.Message;

public class Heater extends SimulationThread {

    public static enum State {

        STOPPED,
        HEATING
    }
    private static final double HEATING_POWER = 100.;
    private Environment environment;
    private State state = State.STOPPED;

    public Heater(Environment environment) {
        this.environment = environment;
    }

    public State getHeaterState() {
        return state;
    }
    
    

    @Override
    protected synchronized void simulationStep() {
        try {
            Message msg = messageQueue.take();
            if (msg instanceof WorkingStateMessage) {
                handleHeaterControlMessage((WorkingStateMessage)msg);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Heater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleHeaterControlMessage(WorkingStateMessage msg) {
        switch (msg.getActivity()) {
            case START:
                if (state == State.STOPPED) {
                    environment.setHeatingPower(HEATING_POWER);
                    state = State.HEATING;
                }
                break;
            case STOP:
                if (state == State.HEATING) {
                    environment.setHeatingPower(0);
                    state = State.STOPPED;
                }
                break;
        }
    }
}
