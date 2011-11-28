
package pralka.sim;


public class WashingMachine {
    Door door;
    Motor motor;
    Heater heater;
    Pump pump;
    ControlUnit controlUnit;
    TemperatureSensor temperatureSensor;
    WaterLevelSensor waterLevelSensor;
    
    public WashingMachine(ComponentFactory factory) {
        door = factory.createDoor();
        motor = factory.createMotor();
        heater = factory.createHeater();
        pump = factory.createPump();
        temperatureSensor = factory.createTemperatureSensor();
        waterLevelSensor = factory.createWaterLevelSensor();        
        controlUnit = factory.createControlUnit(this);
    }
}
