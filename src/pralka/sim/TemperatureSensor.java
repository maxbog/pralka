package pralka.sim;

public class TemperatureSensor {

    private Environment environment;

    public TemperatureSensor(Environment environment) {
        this.environment = environment;
    }

    public double getTemperature() {
        return environment.getWaterTemperature();
    }
}
