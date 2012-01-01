package pralka.sim;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.Message;

public class SimulationThread extends Thread {

    private Simulation simulation;
    private double simulationTime;
    private double timeDelta;
    private boolean shouldStop = false;
    protected BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<Message>(100);

    protected double getSimulationTime() {
        return simulationTime;
    }

    protected double getTimeDelta() {
        return timeDelta;
    }

    protected boolean simulationRunning() {
        simulation.waitWhilePaused();
        if(shouldStop)
            return false;
        final double currentTime = simulation.getCurrentTime();
        timeDelta = (currentTime - simulationTime) / 1000;
        simulationTime = currentTime;
        return true;
    }
    
    public void stopThread() {
        shouldStop = true;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public BlockingQueue<Message> getMessageQueue() {
        return messageQueue;
    }
    
    @Override
    public void run() {
        while(simulationRunning()) {
            simulationStep();
        }
    }
    
    protected void simulationStep() {
        
    }
    
    protected void scheduleMessage(Message msg, SimulationThread destination, int millis) {
        
    }
}
