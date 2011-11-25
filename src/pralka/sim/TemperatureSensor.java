package pralka.sim;

public class TemperatureSensor {
    private Environment environment;

    public TemperatureSensor(Environment environment) {
        this.environment = environment;
    }
    
    public int getTemperature() {
        return environment.getWaterTemperature();
    }
    
}
