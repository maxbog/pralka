package pralka.sim;

public class Pump extends BreakableComponent {

    private static enum State {

        STOPPED,
        PUMPING_INSIDE,
        PUMPING_OUTSIDE
    }

    public static enum Direction {

        OUTSIDE,
        INSIDE
    }
    private Environment environment;
    private State state;
    private static final double litersPerSecond = 1.;

    public Pump(Environment environment) {
        super(10);
        this.environment = environment;
        this.state = State.STOPPED;
    }

    public synchronized void startPumping(Direction direction) {
        if(broken())
            return;
        switch (state) {
            case STOPPED:
                if (direction == Direction.INSIDE) {
                    state = State.PUMPING_INSIDE;
                } else {
                    state = State.PUMPING_OUTSIDE;
                }
                break;
            case PUMPING_INSIDE:
                breakInstantly();
                break;
            case PUMPING_OUTSIDE:
                breakInstantly();
                break;
        }
    }

    public synchronized void stopPumping() {
        if(broken())
            return;
        switch (state) {
            case PUMPING_INSIDE:
                state = State.STOPPED;
                break;
            case PUMPING_OUTSIDE:
                state = State.STOPPED;
                break;
        }
    }

    @Override
    public void run() {
        while (simulationRunning()) {
            synchronized (this) {
                switch (state) {
                    case PUMPING_INSIDE:
                        if (environment.waterLevelTooLow()) {
                            startBreakingTimer();
                        } else {
                            environment.increaseWaterLevel(litersPerSecond * getTimeDelta());
                        }
                    case PUMPING_OUTSIDE:
                        if (environment.waterLevelTooLow()) {
                            startBreakingTimer();
                        } else {
                            environment.decreaseWaterLevel(litersPerSecond * getTimeDelta());
                        }
                }
            }
        }
    }
}
