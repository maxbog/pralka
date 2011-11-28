package pralka.sim;

public class SimulationThread extends Thread {

    private Simulation simulation;
    private double simulationTime;
    private double timeDelta;
    private boolean shouldStop = false;

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
    
    @Override
    public void run() {
        while(simulationRunning()) {
            simulationStep();
        }
    }
    
    protected void simulationStep() {
        
    }
}
