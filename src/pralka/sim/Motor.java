package pralka.sim;

public class Motor extends SimulationThread {

    public Motor() {
    }
    
    public static enum Direction {
        LEFT,
        RIGHT
    }
    
    public static enum State {
        SPINNING,
        STOPPED
    }
    
    private int speed;
    private Direction direction;
    private State state;
    
    public void stopSpinning() {
        if(state == State.STOPPED)
            return;
        state = State.STOPPED;
    }
    
    public void startSpinning(int speed, Direction direction) {
        this.speed = speed;
        this.direction = direction;
        state = State.SPINNING;
    }
    
}
