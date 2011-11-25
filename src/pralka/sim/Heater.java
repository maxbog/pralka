package pralka.sim;

public class Heater extends BreakableComponent {

    private static enum State {

        STOPPED,
        HEATING
    }
    private static final double HEATING_POWER = 100.;
    private Environment environment;
    private State state;

    public Heater(Environment environment) {
        super(10);
        this.environment = environment;
    }

    public synchronized void startHeating() {
        if (broken()) {
            return;
        }
        if (state == State.STOPPED) {
            environment.setHeatingPower(HEATING_POWER);
            state = State.HEATING;
        }
    }

    public synchronized void stopHeating() {
        if (broken()) {
            return;
        }
        if (state == State.HEATING) {
            environment.setHeatingPower(0);
            state = State.STOPPED;
        }
    }

    @Override
    protected synchronized void simulationStep() {
        if (working() && state == State.HEATING && environment.waterLevelTooLow()) {
            startBreakingTimer();
        }
        if (breaking() && !environment.waterLevelTooLow()) {
            stopBreakingTimer();
        }
    }
    
    @Override
    protected synchronized void componentBrokeDown() {
        environment.setHeatingPower(0);
    }
}
