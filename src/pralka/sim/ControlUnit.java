/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pralka.sim;

public class ControlUnit extends SimulationThread {

    private static enum State {
        STOPPED,
        WORKING,
        PAUSED,
        BROKEN
    }
    
    private static enum WorkingState {
        INIT_WASHING,
        PUMPING_INSIDE,
        HEATING,
        WASHING,
        PUMPING_OUTSIDE
    }
    
    private static enum PumpingState {
        PUMPING,
        NOT_PUMPING
    }
    
    private static enum HeatingState {
        HEATING,
        NOT_HEATING,
        FINISHED
    }

    private boolean ensureNotBroken() {
        if (washingMachine.hasBrokenComponent()) {
            state = State.BROKEN;
            washingMachine.setStatus("Zepsuta");
            return false;
        }
        return true;
    }
    
    public enum Substate {

        IDLE,
        INIT_WASHING,
        WASHING,
        RINSING,
        SPINNING
    }
    private Program currentProgram;
    private Substate currentSubstate;
    //  private Pralka pralka;
    private WashingMachine washingMachine;
    
    private WorkingState workingState;
    private State state;

    public ControlUnit(WashingMachine washingMachine) {
        this.washingMachine = washingMachine;
    }

    public void setProgram(Program p) {

        // operacje konieczne do zmiany programu

        currentProgram = p;

    }

    public void setNewSubstate(Substate s) {

        // operacje konieczne do zmiany etapu
        currentSubstate = s;

    }

    public void startWashing() {
        if(state == State.STOPPED) {
            workingState = WorkingState.INIT_WASHING;
            state = State.WORKING;
        }
    }

    @Override
    public void simulationStep() {

        switch(state) {
            case STOPPED:
                if(!ensureNotBroken())
                    break;
            case WORKING:
                if(!ensureNotBroken())
                    break;
            case PAUSED:
                if(!ensureNotBroken())
                    break;
            case BROKEN:
        }
    }
}
