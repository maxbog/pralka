package pralka.sim;

public class Heater extends SimulationThread {
    
    private static enum State {
        STOPPED,
        HEATING,
        BROKEN
    }
    
    private static final double HEATING_POWER = 100.;
    
    private Environment environment;
    private State state;

    public Heater(Environment environment) {
        this.environment = environment;
    }
    
    public void startHeating() {
        if(state == State.STOPPED) {
            environment.setHeatingPower(HEATING_POWER);
            state = State.HEATING;
        }
    }
    
    public void stopHeating() {
        if(state == State.HEATING) {
            environment.setHeatingPower(0);
            state = State.STOPPED;
        }
    }
    
    @Override
    public void run() {
        while(simulationRunning()) {
            if(state == State.HEATING && environment.waterLevelTooLow()) {
                state = State.BROKEN;
                environment.setHeatingPower(0);
            }
        }
    }
    
}
