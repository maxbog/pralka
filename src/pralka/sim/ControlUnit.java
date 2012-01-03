/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pralka.sim;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.ControlUnitMessage;
import pralka.msg.DoorControlMessage;
import pralka.msg.GetMeasurementMessage;
import pralka.msg.InitFinishedMessage;
import pralka.msg.WorkingStateMessage;
import pralka.msg.Message;
import pralka.msg.PumpControllerMessage;
import pralka.msg.PumpingFinishedMessage;
import pralka.msg.TemperatureControllerMessage;
import pralka.msg.WaitingFinishedMessage;
import pralka.msg.WashingFinishedMessage;
import pralka.msg.WaterLevelMessage;

public class ControlUnit extends SimulationThread {
    private final double WAITING_AFTER_WASHING_TIME = 5*60.;
    
    private PumpController pumpController;
    private TemperatureController tempController;
    private WashingController washingController;
    private double washingRemainingTime;
    private double washingStartedTime;
    
    private void processWorkingState(Message msg) throws InterruptedException {
        if(msg instanceof ControlUnitMessage) {
            ControlUnitMessage cuMessage = (ControlUnitMessage) msg;
            if(cuMessage.getActivity() == ControlUnitMessage.Activity.STOP) {
                workingStates.clear();
                washingController.send(new WorkingStateMessage(WorkingStateMessage.Activity.STOP));
                tempController.send(new TemperatureControllerMessage(0, WorkingStateMessage.Activity.STOP));
                washingMachine.getWaterLevelSensor().send(new GetMeasurementMessage(this));
                state = State.STOPPING;
            }
        }
        
        if(workingStates.contains(WorkingState.INIT_WASHING) && msg instanceof InitFinishedMessage) {
            workingStates.clear();
            workingStates.addAll(Arrays.asList(WorkingState.HEATING, WorkingState.WASHING));
            tempController.send(new TemperatureControllerMessage(currentProgram.getTemperature(), WorkingStateMessage.Activity.START));
            pumpController.send(new PumpControllerMessage(Pump.Direction.INSIDE, WorkingStateMessage.Activity.START));
        }
        
        if(workingStates.contains(WorkingState.PUMPING_INSIDE) && msg instanceof PumpingFinishedMessage) {
            workingStates.remove(WorkingState.PUMPING_INSIDE);
            workingStates.add(WorkingState.WASHING);
            washingController.send(new WorkingStateMessage(WorkingStateMessage.Activity.START));
            washingRemainingTime = currentStage == WashingStage.WASHING ? currentProgram.getWashingTime() : currentProgram.getRinsingTime();
            washingStartedTime = getSimulationTime();
            scheduleMessage(new WashingFinishedMessage(), washingRemainingTime);
        }
        
        if(workingStates.containsAll(Arrays.asList(WorkingState.HEATING, WorkingState.WASHING)) && msg instanceof WashingFinishedMessage) {
            workingStates.clear();
            workingStates.add(WorkingState.PUMPING_OUTSIDE);
            tempController.send(new TemperatureControllerMessage(0., WorkingStateMessage.Activity.STOP));
            washingController.send(new WorkingStateMessage(WorkingStateMessage.Activity.STOP));
            pumpController.send(new PumpControllerMessage(Pump.Direction.OUTSIDE, WorkingStateMessage.Activity.START));
        }
        
        if(workingStates.contains(WorkingState.PUMPING_OUTSIDE) && msg instanceof PumpingFinishedMessage) {
            workingStates.clear();
            pumpController.send(new PumpControllerMessage(null, WorkingStateMessage.Activity.STOP));
            if(currentStage != WashingStage.RINSING) {
                currentStage = WashingStage.RINSING;
                workingStates.add(WorkingState.INIT_WASHING);
                scheduleMessage(new InitFinishedMessage(), 10);
            } else {
                washingController.send(new WorkingStateMessage(WorkingStateMessage.Activity.START));
                scheduleMessage(new WashingFinishedMessage(), currentProgram.getSpinningTime());
                workingStates.add(WorkingState.SPINNING);
            }
        }
        
        if(workingStates.contains(WorkingState.SPINNING)  && msg instanceof WashingFinishedMessage) {
            workingStates.clear();
            workingStates.add(WorkingState.WAITING_AFTER_WASHING);
            scheduleMessage(new WaitingFinishedMessage(), WAITING_AFTER_WASHING_TIME);
        }
        
        if(workingStates.contains(WorkingState.WAITING_AFTER_WASHING) && msg instanceof WaitingFinishedMessage) {
            workingStates.clear();
            washingMachine.getDoor().send(new DoorControlMessage(DoorControlMessage.Activity.UNLOCK));
            state = State.STOPPED;
        }
    }

    private void processStopedState(Message msg) throws InterruptedException {
        if(msg instanceof ControlUnitMessage) {
            ControlUnitMessage cuMessage = (ControlUnitMessage) msg;
            if(cuMessage.getActivity() == ControlUnitMessage.Activity.START && !washingMachine.getDoor().isOpen()) {
                washingMachine.getDoor().send(new DoorControlMessage(DoorControlMessage.Activity.LOCK));
                workingStates.clear();
                workingStates.add(WorkingState.INIT_WASHING);
                state = State.WORKING;
            }
        }
    }

    private void processStoppingState(Message msg) throws InterruptedException {
        if(msg instanceof WaterLevelMessage) {
            if(((WaterLevelMessage)msg).isAboveLowLevel()) {
                pumpController.send(new PumpControllerMessage(Pump.Direction.OUTSIDE, WorkingStateMessage.Activity.START));
            }
        }
        if(msg instanceof PumpingFinishedMessage) {
            washingMachine.getDoor().send(new DoorControlMessage(DoorControlMessage.Activity.UNLOCK));
            state = State.STOPPED;
        }
    }

    private static enum State {
        STOPPED,
        STOPPING,
        WORKING,
        PAUSED
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
    private WashingMachine washingMachine;
    
    private EnumSet workingStates = EnumSet.noneOf(WorkingState.class);
    private State state;

    public ControlUnit(WashingMachine washingMachine) {
        this.washingMachine = washingMachine;
        pumpController = simulation.registeringThread(new PumpController(this, washingMachine.getWaterLevelSensor(), washingMachine.getPump()));
        tempController = simulation.registeringThread(new TemperatureController(washingMachine.getTemperatureSensor(), washingMachine.getHeater()));
        washingController = simulation.registeringThread(new WashingController());
    }

    @Override
    public void simulationStep() {
        try {
            Message msg = messageQueue.take();
            switch(state) {
                case STOPPED:
                    processStopedState(msg);
                case STOPPING:
                    processStoppingState(msg);
                case WORKING:
                    processWorkingState(msg);
                case PAUSED:
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlUnit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
