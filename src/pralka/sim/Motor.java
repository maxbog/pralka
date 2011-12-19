package pralka.sim;

public class Motor extends BreakableComponent {

    public Motor() {
        super(10);
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
        if(broken() || state == State.STOPPED)
            return;
        state = State.STOPPED;
    }
    
    public void startSpinning(int speed, Direction direction) {
        if(state == State.SPINNING && this.direction != direction)
            breakInstantly();
        if(broken())
            return;
        this.speed = speed;
        this.direction = direction;
        state = State.SPINNING;
    }
    
}
