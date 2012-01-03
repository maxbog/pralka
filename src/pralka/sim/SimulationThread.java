package pralka.sim;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.Message;

public class SimulationThread extends Thread {

    protected Simulation simulation;
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
        timeDelta = currentTime - simulationTime;
        simulationTime = currentTime;
        return true;
    }
    
    public void stopThread() {
        try {
            shouldStop = true;
            // puszczamy dowolną wiadomość, żeby warunek był ponownie sprawdzony
            send(new Message());
        } catch (InterruptedException ex) {
            Logger.getLogger(SimulationThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }
    
    public void send(Message msg) throws InterruptedException {
        messageQueue.put(msg);
    }
    
    @Override
    public void run() {
        while(simulationRunning()) {
            simulationStep();
        }
    }
    
    protected void simulationStep() {
        
    }
    
    protected void scheduleMessage(Message msg, double time) {
        simulation.scheduleMessage(msg, this, time);
    }
}
