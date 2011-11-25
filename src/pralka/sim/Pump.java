package pralka.sim;

public class Pump extends SimulationThread {
    
    private static enum State {
        STOPPED,
        PUMPING_INSIDE,
        PUMPING_OUTSIDE,
        BROKEN
    }
    
    public static enum Direction {
        OUTSIDE,
        INSIDE
    }
    
    private Environment environment;
    private State state;
    private static final double litersPerSecond = 1.;

    public Pump(Environment environment) {
        this.environment = environment;
        this.state = State.STOPPED;
    }
    
    public synchronized void startPumping(Direction direction) {
        switch(state) {
            case STOPPED:
                if(direction == Direction.INSIDE)
                    state = State.PUMPING_INSIDE;
                else
                    state = State.PUMPING_OUTSIDE;
                break;
            case PUMPING_INSIDE:
                state = State.BROKEN;
                break;
            case PUMPING_OUTSIDE:
                state = State.BROKEN;
                break;
        }
    }
    
    public synchronized void stopPumping() {
        switch(state) {
            case PUMPING_INSIDE:
                state = State.STOPPED;
                break;
            case PUMPING_OUTSIDE:
                state = State.STOPPED;
                break;
        }
    }
    
    @Override
    public synchronized void run() {
        while(simulationRunning()) {
            switch(state) {
                case PUMPING_INSIDE:
                    if(environment.waterLevelTooLow())
                        state = State.BROKEN;
                    else
                        environment.increaseWaterLevel(litersPerSecond*getTimeDelta());
                case PUMPING_OUTSIDE:
                    if(environment.waterLevelTooLow())
                        state = State.BROKEN;
                    else
                        environment.decreaseWaterLevel(litersPerSecond*getTimeDelta());
            }
        }
    }
}
