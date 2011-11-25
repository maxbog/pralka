package pralka.sim;

public class SimulationThread extends Thread {
    private Timer timer;
    private int simulationTime;
    private double timeDelta;
    protected int getSimulationTime() {
        return simulationTime;
    }
    
    protected double getTimeDelta() {
        return timeDelta;
    }
    
    protected boolean simulationRunning() {
        //TODO: jeśli symulacja się kończy - zwrócić false
        timer.waitWhilePaused();
        final int currentTime = timer.getCurrentTime();
        timeDelta = (currentTime - simulationTime) / 1000;
        simulationTime = currentTime;
        return true;
    }
}
