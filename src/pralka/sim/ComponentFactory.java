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
        return simulation.registeringThread(new Door());
    }
    
    public Motor createMotor() {
        return simulation.registeringThread(new Motor());
    }
    
    public TemperatureSensor createTemperatureSensor() {
        return simulation.registeringThread(new TemperatureSensor(environment));
    }
    
    public WaterLevelSensor createWaterLevelSensor() {
        return simulation.registeringThread(new WaterLevelSensor(environment));
    }
    
    public ControlUnit createControlUnit(WashingMachine washingMachine) {
        TemperatureController tempCtrl = simulation.registeringThread(new TemperatureController(washingMachine.getTemperatureSensor(), washingMachine.getHeater()));
        PumpController pumpCtrl = simulation.registeringThread(new PumpController(washingMachine.getWaterLevelSensor(), washingMachine.getPump()));
        WashingController washingCtrl = simulation.registeringThread(new WashingController(washingMachine.getMotor()));
        return simulation.registeringThread(new ControlUnit(pumpCtrl, tempCtrl, washingCtrl, washingMachine));
    }
    
    public Programmer createProgrammer(WashingMachine washingMachine) {
        return new Programmer(washingMachine);
    }
}
