package pralka.sim;

public class SimulationThread extends Thread {

    private Timer timer;
    private double simulationTime;
    private double timeDelta;

    protected double getSimulationTime() {
        return simulationTime;
    }

    protected double getTimeDelta() {
        return timeDelta;
    }

    protected boolean simulationRunning() {
        //TODO: jeśli symulacja się kończy - zwrócić false
        timer.waitWhilePaused();
        final double currentTime = timer.getCurrentTime();
        timeDelta = (currentTime - simulationTime) / 1000;
        simulationTime = currentTime;
        return true;
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
