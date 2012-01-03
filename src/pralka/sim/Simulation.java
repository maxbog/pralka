package pralka.sim;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.Message;
import pralka.msg.PumpControllerMessage;
import pralka.msg.TemperatureControllerMessage;
import pralka.msg.WorkingStateMessage.Activity;

public class Simulation {
    Environment environment;
    WashingMachine washingMachine;
    Timer timer;
    WaterLevelSensor sensor;
    Pump pump;
    ControlUnit controlUnit;
    PumpController pumpCtrl;
    
    ArrayList<SimulationThread> threads;

    public Simulation() {
        timer = new Timer();
        timer.start();
        threads = new ArrayList<SimulationThread>();
        environment = registeringThread(new Environment());
        washingMachine = new WashingMachine(new ComponentFactory(environment, this));
    }
    
    public final <T extends SimulationThread> T registeringThread(T thread) {
        thread.setSimulation(this);
        threads.add(thread);
        return thread;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public WashingMachine getWashingMachine() {
        return washingMachine;
    }
    
    

    public void waitWhilePaused() {
        timer.waitWhilePaused();
    }
    
    public double getCurrentTime() {
        return timer.getCurrentTime();
    }
    
    public void start() {
        for(SimulationThread thread : threads) {
            thread.start();
        }
        timer.resumeTimer();
    }
        
    public void stop() {
        for(SimulationThread thread : threads) {
            thread.stopThread();
        }
        timer.resumeTimer();
    }
    
    public void pause() {
        timer.pause();
    }
    
    public void resume() {
        timer.resumeTimer();
    }
    
    public void scheduleMessage(Message msg, SimulationThread destination, double time) {
        timer.scheduleMessage(msg, destination, time);
    }

    void clearScheduledMessages(SimulationThread destination) {
        timer.clearScheduledMessages(destination);
    }
}
