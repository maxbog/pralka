package pralka.sim;

public class BreakableComponent extends SimulationThread {
    private static enum Condition {
        WORKING,
        BREAKING,
        BROKEN
    }
    
    private Condition condition;
    private double breakTime;
    private double breakingStartTime;    
    
    public boolean working() {
        return condition == Condition.WORKING;
    }    
    
    public boolean breaking() {
        return condition == Condition.BREAKING;
    }
    
    public boolean broken() {
        return condition == Condition.BROKEN;
    }

    public BreakableComponent(double breakTime) {
        this.condition = Condition.WORKING;
        this.breakTime = breakTime;
    }
    
    protected void startBreakingTimer() {
        if(condition == Condition.BROKEN)
            return;
        breakingStartTime = getSimulationTime();
        condition = Condition.BREAKING;
    }
    
    protected void checkBreakingTimer() {
        if(condition != Condition.BREAKING)
            return;
        if(getSimulationTime() >= breakingStartTime + breakTime)
            condition = Condition.BROKEN;
    }
    
    protected void stopBreakingTimer() {
        if(condition != Condition.BREAKING)
            return;
        condition = Condition.WORKING;
    }    
    
    protected void breakInstantly() {
        condition = Condition.BROKEN;
    }
    
    @Override
    public void run() {
        while(simulationRunning()) {
            checkBreakingTimer();
            if(broken()) {
                componentBrokeDown();
                break;
            }
            simulationStep();
        }
    }
    
    protected void componentBrokeDown() {
        
    }
}
