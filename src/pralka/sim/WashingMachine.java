
package pralka.sim;

import com.jgoodies.binding.beans.Model;


public class WashingMachine extends Model {
    Door door;
    Motor motor;
    Heater heater;
    Pump pump;
    ControlUnit controlUnit;
    TemperatureSensor temperatureSensor;
    WaterLevelSensor waterLevelSensor;
    Programmer programmer;
    
    public WashingMachine(ComponentFactory factory) {
        door = factory.createDoor();
        motor = factory.createMotor();
        heater = factory.createHeater();
        pump = factory.createPump();
        temperatureSensor = factory.createTemperatureSensor();
        waterLevelSensor = factory.createWaterLevelSensor();        
        controlUnit = factory.createControlUnit(this);
        programmer = factory.createProgrammer(this);
    }

    public ControlUnit getControlUnit() {
        return controlUnit;
    }

    public Programmer getProgrammer() {
        return programmer;
    }

    public Heater getHeater() {
        return heater;
    }

    public Pump getPump() {
        return pump;
    }

    public TemperatureSensor getTemperatureSensor() {
        return temperatureSensor;
    }

    public WaterLevelSensor getWaterLevelSensor() {
        return waterLevelSensor;
    }

    public Door getDoor() {
        return door;
    }

    public Motor getMotor() {
        return motor;
    }
    
    
    
    public boolean hasBrokenComponent() {
        return heater.broken() || pump.broken() || motor.broken();
    }
    
    //<editor-fold defaultstate="collapsed" desc="Property - status">
    public static final String PROPERTYNAME_STATUS = "status";
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if(status == null ? this.status == null : status.equals(this.status))
            return;
        String oldValue = this.status;
        this.status = status;
        firePropertyChange(PROPERTYNAME_STATUS, oldValue, status);        
    }
    //</editor-fold>
    
    
}
