package pralka.sim;

import java.util.ArrayList;

public class Simulation {
    Environment environment;
    WashingMachine washingMachine;
    Timer timer;
    
    ArrayList<SimulationThread> threads;

    public Simulation() {
        timer = new Timer();
        threads = new ArrayList<SimulationThread>();
        environment = registeringThread(new Environment());
        washingMachine = new WashingMachine(new ComponentFactory(environment, this));
    }
    
    public final <T extends SimulationThread> T registeringThread(T thread) {
        thread.setSimulation(this);
        threads.add(thread);
        return thread;
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
        timer.resume();
    }
        
    public void stop() {
        for(SimulationThread thread : threads) {
            thread.stopThread();
        }
        timer.resume();
    }
    
    public void pause() {
        timer.pause();
    }
    
    public void resume() {
        timer.resume();
    }
}
