package pralka.sim;

public class ComponentFactory {
    Environment environment;
    Simulation simulation;

    public ComponentFactory(Environment environment, Simulation simulation) {
        this.environment = environment;
        this.simulation = simulation;
    }
    
    public Pump createPump() {
        return simulation.registeringThread(new Pump(environment));
    }
    
    public Heater createHeater() {
        return simulation.registeringThread(new Heater(environment));        
    }
    
    public Door createDoor() {
        return new Door();
    }
    
    public Motor createMotor() {
        return new Motor();
    }
    
    public TemperatureSensor createTemperatureSensor() {
        return new TemperatureSensor(environment);
    }
    
    public WaterLevelSensor createWaterLevelSensor() {
        return new WaterLevelSensor(environment);
    }
    
    public ControlUnit createControlUnit(WashingMachine washingMachine) {
        return new ControlUnit(washingMachine);
    }
}
