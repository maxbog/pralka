package pralka.msg;

public class TemperatureControllerMessage extends WorkingStateMessage {
    double targetTemperature;

    public TemperatureControllerMessage(double targetTemperature, Activity activity) {
        super(activity);
        this.targetTemperature = targetTemperature;
    }

    public double getTargetTemperature() {
        return targetTemperature;
    }
    
    
}
