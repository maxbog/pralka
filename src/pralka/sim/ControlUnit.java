/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pralka.sim;

import java.util.Arrays;
import java.util.EnumSet;

public class ControlUnit extends SimulationThread {
    private final double EPSILON = 2.;
    private final double WAITING_AFTER_WASHING_TIME = 5*60.;

    private double countingStartTime;
    
    private void processWorkingState() {
        if(workingStates.containsAll(Arrays.asList(WorkingState.HEATING, WorkingState.WASHING)) 
                && heatingState == HeatingState.FINISHED 
                && washingState == WashingState.FINISHED) {
            workingStates.clear();
            workingStates.add(WorkingState.PUMPING_OUTSIDE);
        }
        
        if(workingStates.contains(WorkingState.PUMPING_OUTSIDE) && pumpingState == PumpingState.NOT_PUMPING) {
            workingStates.clear();
            if(currentStage != WashingStage.RINSING) {
                currentStage = currentStage == WashingStage.FIRST_WASHING ? WashingStage.SECOND_WASHING : WashingStage.RINSING;
                workingStates.add(WorkingState.INIT_WASHING);
            } else {
                countingStartTime = getSimulationTime();
                washingMachine.getMotor().startSpinning(currentProgram.getSpinningSpeed(), Motor.Direction.LEFT);
                workingStates.add(WorkingState.SPINNING);
            }
        }
        
        if(workingStates.contains(WorkingState.SPINNING) && spinningState == SpinningState.NOT_SPINNING) {
            workingStates.clear();
            countingStartTime = getSimulationTime();            
            workingStates.add(WorkingState.WAITING_AFTER_WASHING);
        }
        
        if(workingStates.contains(WorkingState.HEATING)) {
            switch(heatingState) {
                case HEATING:
                    if(currentProgram.getTemperature() + EPSILON < washingMachine.getTemperatureSensor().getTemperature()) {
                        heatingState = HeatingState.NOT_HEATING;
                        washingMachine.getHeater().stopHeating();
                    }
                case NOT_HEATING:
                    if(currentProgram.getTemperature() - EPSILON > washingMachine.getTemperatureSensor().getTemperature()) {
                        heatingState = HeatingState.HEATING;
                        washingMachine.getHeater().startHeating();
                    }
            }
        }
        
        if(workingStates.contains(WorkingState.PUMPING_INSIDE) && pumpingState == PumpingState.PUMPING) {
            if(washingMachine.getWaterLevelSensor().aboveHighLevel()) {
                pumpingState = PumpingState.NOT_PUMPING;
            }
        }
        
        if(workingStates.contains(WorkingState.WASHING)) {
            
        }
        
        if(workingStates.contains(WorkingState.PUMPING_OUTSIDE) && pumpingState == PumpingState.PUMPING) {
            if(!washingMachine.getWaterLevelSensor().aboveLowLevel()) {
                pumpingState = PumpingState.NOT_PUMPING;
            }
        }
        
        if(workingStates.contains(WorkingState.SPINNING)) {
            if(spinningState == SpinningState.SPINNING && getSimulationTime() >= countingStartTime + currentProgram.getSpinningTime()) {
                washingMachine.getMotor().stopSpinning();
                spinningState = SpinningState.NOT_SPINNING;
            }
        }       
        
        if(workingStates.contains(WorkingState.WAITING_AFTER_WASHING) && getSimulationTime() >= countingStartTime + WAITING_AFTER_WASHING_TIME) {
            washingMachine.getDoor().unlock();
            state = State.STOPPED;
        }
    }

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
        PUMPING_OUTSIDE,
        SPINNING,
        WAITING_AFTER_WASHING
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
    
    private static enum WashingState {
        WASH_LEFT,
        WASH_RIGHT,
        FINISHED
    }
    
    private static enum SpinningState {
        SPINNING,
        NOT_SPINNING
    }

    private boolean ensureNotBroken() {
        if (washingMachine.hasBrokenComponent()) {
            state = State.BROKEN;
            washingMachine.setStatus("Zepsuta");
            return false;
        }
        return true;
    }
    
    public enum WashingStage {
        FIRST_WASHING,
        SECOND_WASHING,
        RINSING
    }
    private Program currentProgram;
    private WashingStage currentStage;
    //  private Pralka pralka;
    private WashingMachine washingMachine;
    
    private EnumSet workingStates = EnumSet.noneOf(WorkingState.class);
    private State state;
    private HeatingState heatingState;
    private PumpingState pumpingState;
    private WashingState washingState;
    private SpinningState spinningState;

    public ControlUnit(WashingMachine washingMachine) {
        this.washingMachine = washingMachine;
    }

    public void setProgram(Program p) {

        // operacje konieczne do zmiany programu

        currentProgram = p;

    }

    public void startWashing() {
        if(state == State.STOPPED) {
            workingStates.clear();
            workingStates.add(WorkingState.INIT_WASHING);
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
                processWorkingState();
            case PAUSED:
                if(!ensureNotBroken())
                    break;
            case BROKEN:
        }
    }
}
