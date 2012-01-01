package pralka.msg;

public class TemperatureMessage extends Message {
    private double measurement;

    public TemperatureMessage(double measurement) {
        this.measurement = measurement;
    }

    public double getMeasurement() {
        return measurement;
    }
}
