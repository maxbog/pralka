package pralka.sim;

public class Pump {
    
    public static enum Direction {
        OUTSIDE,
        INSIDE
    }
    
    private Environment environment;
    private Direction direction;

    public Pump(Environment environment) {
        this.environment = environment;
    }
}
