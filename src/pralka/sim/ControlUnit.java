/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pralka.sim;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.GetMeasurementMessage;
import pralka.msg.InitFinishedMessage;
import pralka.msg.WorkingStateMessage;
import pralka.msg.Message;
import pralka.msg.PumpControllerMessage;
import pralka.msg.PumpingFinishedMessage;
import pralka.msg.TemperatureControllerMessage;
import pralka.msg.TemperatureMessage;
import pralka.msg.WaitingFinishedMessage;
import pralka.msg.WashingFinishedMessage;
import pralka.msg.WaterLevelMessage;

public class ControlUnit extends SimulationThread {
    private final double EPSILON = 2.;
    private final double WAITING_AFTER_WASHING_TIME = 5*60.;

    private double countingStartTime;
    
    private PumpController pumpController;
    private TemperatureController tempController;
    private WashingController washingController;
    
    private void processWorkingState(Message msg) throws InterruptedException {
        
        if(workingStates.contains(WorkingState.INIT_WASHING) && msg instanceof InitFinishedMessage) {
            workingStates.clear();
            workingStates.addAll(Arrays.asList(WorkingState.HEATING, WorkingState.WASHING));
            tempController.getMessageQueue().put(new TemperatureControllerMessage(currentProgram.getTemperature(), WorkingStateMessage.Activity.START));
            pumpController.getMessageQueue().put(new PumpControllerMessage(Pump.Direction.INSIDE, WorkingStateMessage.Activity.START));
        }
        
        if(workingStates.contains(WorkingState.PUMPING_INSIDE) && msg instanceof PumpingFinishedMessage) {
            workingStates.remove(WorkingState.PUMPING_INSIDE);
            workingStates.add(WorkingState.WASHING);
            washingController.getMessageQueue().put(new WorkingStateMessage(WorkingStateMessage.Activity.START));
            double washingTime = currentStage == WashingStage.WASHING ? currentProgram.getWashingTime() : currentProgram.getRinsingTime();
            scheduleMessage(new WashingFinishedMessage(), this, washingTime);
        }
        
        if(workingStates.containsAll(Arrays.asList(WorkingState.HEATING, WorkingState.WASHING)) && msg instanceof WashingFinishedMessage) {
            workingStates.clear();
            workingStates.add(WorkingState.PUMPING_OUTSIDE);
            tempController.getMessageQueue().put(new TemperatureControllerMessage(0., WorkingStateMessage.Activity.STOP));
            washingController.getMessageQueue().put(new WorkingStateMessage(WorkingStateMessage.Activity.STOP));
            pumpController.getMessageQueue().put(new PumpControllerMessage(Pump.Direction.OUTSIDE, WorkingStateMessage.Activity.START));
        }
        
        if(workingStates.contains(WorkingState.PUMPING_OUTSIDE) && msg instanceof PumpingFinishedMessage) {
            workingStates.clear();
            pumpController.getMessageQueue().put(new PumpControllerMessage(null, WorkingStateMessage.Activity.STOP));
            if(currentStage != WashingStage.RINSING) {
                currentStage = WashingStage.RINSING;
                workingStates.add(WorkingState.INIT_WASHING);
                scheduleMessage(new InitFinishedMessage(), this, 10);
            } else {
                washingController.getMessageQueue().put(new WorkingStateMessage(WorkingStateMessage.Activity.START));
                scheduleMessage(new WashingFinishedMessage(), this, currentProgram.getSpinningTime());
                workingStates.add(WorkingState.SPINNING);
            }
        }
        
        if(workingStates.contains(WorkingState.SPINNING)  && msg instanceof WashingFinishedMessage) {
            workingStates.clear();
            workingStates.add(WorkingState.WAITING_AFTER_WASHING);
            scheduleMessage(new WaitingFinishedMessage(), this, WAITING_AFTER_WASHING_TIME);
        }
        
        if(workingStates.contains(WorkingState.WAITING_AFTER_WASHING) && msg instanceof WaitingFinishedMessage) {
            workingStates.clear();
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
    
    public enum WashingStage {
        WASHING,
        RINSING
    }
    private Program currentProgram;
    private WashingStage currentStage;
    //  private Pralka pralka;
    private WashingMachine washingMachine;
    
    private EnumSet workingStates = EnumSet.noneOf(WorkingState.class);
    private State state;

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
        try {
            Message msg = messageQueue.take();
            scheduleMeasurements(msg);
            switch(state) {
                case STOPPED:
                case WORKING:
                    processWorkingState(msg);
                case PAUSED:
                case BROKEN:
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlUnit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void scheduleMeasurements(Message msg) {
        if(msg instanceof TemperatureMessage) {
            scheduleMessage(new GetMeasurementMessage(this),washingMachine.getTemperatureSensor(), 1000);
        } else if(msg instanceof WaterLevelMessage) {
            scheduleMessage(new GetMeasurementMessage(this),washingMachine.getWaterLevelSensor(), 1000);
        }
    }
}
