package pralka.sim;

public class WaterLevelSensor {

    private static final int LOW_LEVEL = 90;
    private static final int HIGH_LEVEL = 100;
    private Environment environment;

    public WaterLevelSensor(Environment environment) {
        this.environment = environment;
    }

    public boolean aboveLowLevel() {
        return environment.getWaterLevel() >= LOW_LEVEL;
    }

    public boolean aboveHighLevel() {
        return environment.getWaterLevel() >= HIGH_LEVEL;
    }
}
