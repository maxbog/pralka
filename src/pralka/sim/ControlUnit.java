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
import pralka.msg.WashingControllerMessage;
import pralka.msg.WashingFinishedMessage;
import pralka.msg.WaterLevelMessage;

public class ControlUnit extends SimulationThread {

    private static final int INIT_TIME = 20;
    private final double WAITING_AFTER_WASHING_TIME = 5. * 60;

    public State getControlUnitState() {
        return state;
    }

    public static enum State {

        STOPPED,
        STOPPING,
        WORKING,
        PAUSED
    }

    public static enum WorkingState {

        INIT_WASHING,
        PUMPING_INSIDE,
        HEATING,
        WASHING,
        PUMPING_OUTSIDE,
        SPINNING,
        WAITING_AFTER_WASHING
    }

    public static enum WashingStage {

        INITIAL_WASHING,
        WASHING,
        RINSING
    }
    private Program currentProgram;
    private WashingStage currentStage;
    private WashingMachine washingMachine;
    private EnumSet workingStates = EnumSet.noneOf(WorkingState.class);
    private State state;
    private PumpController pumpController;
    private TemperatureController tempController;
    private WashingController washingController;
    private double operationRemainingTime;
    private double operationElapsedTime;
    private double operationStartedTime;
    private boolean cradleMoves;

    private void processWorkingState(Message msg) throws InterruptedException {
        if (msg instanceof ControlUnitMessage) {
            ControlUnitMessage cuMessage = (ControlUnitMessage) msg;
            if (cuMessage.getActivity() == ControlUnitMessage.Activity.STOP) {
                stopWashing();
            }
            if (cuMessage.getActivity() == ControlUnitMessage.Activity.PAUSE) {
                clearScheduledMessages();
                computeRemainingOperationTime();
                washingController.send(new WashingControllerMessage(cradleMoves, WorkingStateMessage.Activity.STOP));
                tempController.send(new TemperatureControllerMessage(0, WorkingStateMessage.Activity.STOP));
                pumpController.send(new PumpControllerMessage(null, WorkingStateMessage.Activity.STOP));
                state = State.PAUSED;
                washingMachine.setStatus("Pauza");
            }
        }

        if (workingStates.contains(WorkingState.INIT_WASHING) && msg instanceof InitFinishedMessage) {
            workingStates.clear();
            workingStates.addAll(Arrays.asList(WorkingState.HEATING, WorkingState.PUMPING_INSIDE));
            tempController.send(new TemperatureControllerMessage(currentProgram.getTemperature(), WorkingStateMessage.Activity.START));
            pumpController.send(new PumpControllerMessage(Pump.Direction.INSIDE, WorkingStateMessage.Activity.START));
            washingMachine.setStatus("Pobieranie wody");
        }

        if (workingStates.contains(WorkingState.PUMPING_INSIDE) && msg instanceof PumpingFinishedMessage) {
            workingStates.remove(WorkingState.PUMPING_INSIDE);
            workingStates.add(WorkingState.WASHING);
            washingController.send(new WashingControllerMessage(cradleMoves, WorkingStateMessage.Activity.START));
            scheduleMessageWithTime(new WashingFinishedMessage(), getStageTime());
            washingMachine.setStatus(getStageStatus());
        }

        if (workingStates.containsAll(Arrays.asList(WorkingState.HEATING, WorkingState.WASHING)) && msg instanceof WashingFinishedMessage) {
            workingStates.clear();
            workingStates.add(WorkingState.PUMPING_OUTSIDE);
            tempController.send(new TemperatureControllerMessage(0., WorkingStateMessage.Activity.STOP));
            washingController.send(new WashingControllerMessage(cradleMoves, WorkingStateMessage.Activity.STOP));
            pumpController.send(new PumpControllerMessage(Pump.Direction.OUTSIDE, WorkingStateMessage.Activity.START));
            washingMachine.setStatus("Wypompowywanie wody");
        }

        if (workingStates.contains(WorkingState.PUMPING_OUTSIDE) && msg instanceof PumpingFinishedMessage) {
            workingStates.clear();
            pumpController.send(new PumpControllerMessage(null, WorkingStateMessage.Activity.STOP));
            if (currentStage != WashingStage.RINSING) {
                currentStage = currentStage == WashingStage.INITIAL_WASHING ? WashingStage.WASHING : WashingStage.RINSING;
                workingStates.add(WorkingState.INIT_WASHING);
                scheduleMessageWithTime(new InitFinishedMessage(), INIT_TIME);
                washingMachine.setStatus("Pobieranie płynu do płukania");
            } else {
                washingController.send(new WashingControllerMessage(cradleMoves, WorkingStateMessage.Activity.START));
                scheduleMessageWithTime(new WashingFinishedMessage(), currentProgram.getSpinningTime());
                workingStates.add(WorkingState.SPINNING);
                washingMachine.setStatus("Wirowanie");
            }
        }

        if (workingStates.contains(WorkingState.SPINNING) && msg instanceof WashingFinishedMessage) {
            workingStates.clear();
            workingStates.add(WorkingState.WAITING_AFTER_WASHING);
            washingController.send(new WashingControllerMessage(cradleMoves, WorkingStateMessage.Activity.STOP));
            scheduleMessageWithTime(new WaitingFinishedMessage(), WAITING_AFTER_WASHING_TIME);
            washingMachine.setStatus("Zaczekaj na odblokowanie drzwi");
        }

        if (workingStates.contains(WorkingState.WAITING_AFTER_WASHING) && msg instanceof WaitingFinishedMessage) {
            workingStates.clear();
            washingMachine.getDoor().send(new DoorControlMessage(DoorControlMessage.Activity.UNLOCK));
            state = State.STOPPED;
            washingMachine.setStatus("Pranie zakończone. Wybierz program i wciśnij \"Start\"");
        }
    }

    private Integer getStageTime() {
        return currentStage == WashingStage.RINSING ? currentProgram.getRinsingTime() : currentProgram.getWashingTime();
    }

    private String getStageStatus() {
        switch (currentStage) {
            case INITIAL_WASHING:
                return "Pranie wstępne";
            case RINSING:
                return "Płukanie";
            case WASHING:
                return "Pranie";
            default:
                return null;
        }
    }

    private void scheduleMessageWithTime(Message msg, double time) {
        operationStartedTime = getSimulationTime();
        operationElapsedTime = 0.;
        scheduleMessage(msg, time);
    }

    public EnumSet getWorkingStates() {
        return workingStates;
    }

    private void computeRemainingOperationTime() {
        operationElapsedTime = getSimulationTime() - operationStartedTime;

        if (workingStates.contains(WorkingState.INIT_WASHING)) {
            operationRemainingTime = INIT_TIME - operationElapsedTime;
        } else if (workingStates.contains(WorkingState.WASHING)) {
            operationRemainingTime = currentStage == WashingStage.WASHING ? currentProgram.getWashingTime() : currentProgram.getRinsingTime();
        } else if (workingStates.contains(WorkingState.SPINNING)) {
            operationRemainingTime = currentProgram.getSpinningTime();
        }
    }

    private void stopWashing() throws InterruptedException {
        clearScheduledMessages();
        workingStates.clear();
        washingController.send(new WashingControllerMessage(cradleMoves, WorkingStateMessage.Activity.STOP));
        tempController.send(new TemperatureControllerMessage(0, WorkingStateMessage.Activity.STOP));
        washingMachine.getWaterLevelSensor().send(new GetMeasurementMessage(this));
        state = State.STOPPING;
        washingMachine.setStatus("Zatrzymywanie");
    }

    private void processStopedState(Message msg) throws InterruptedException {
        if (msg instanceof ControlUnitMessage) {
            ControlUnitMessage cuMessage = (ControlUnitMessage) msg;
            if (cuMessage.getActivity() == ControlUnitMessage.Activity.START && !washingMachine.getDoor().isOpen()) {
                washingMachine.getDoor().send(new DoorControlMessage(DoorControlMessage.Activity.LOCK));
                currentProgram = cuMessage.getChosenProgram();
                cradleMoves = currentProgram.doCradleMoves();
                workingStates.clear();
                workingStates.add(WorkingState.INIT_WASHING);
                scheduleMessageWithTime(new InitFinishedMessage(), INIT_TIME);
                currentStage = cuMessage.doInitialWashing() ? WashingStage.INITIAL_WASHING : WashingStage.WASHING;
                state = State.WORKING;
                washingMachine.setStatus("Pobieranie proszku do prania");
            }
        }
    }

    private void processStoppingState(Message msg) throws InterruptedException {
        if (msg instanceof WaterLevelMessage) {
            if (((WaterLevelMessage) msg).isAboveLowLevel()) {
                pumpController.send(new PumpControllerMessage(Pump.Direction.OUTSIDE, WorkingStateMessage.Activity.START));
            } else {
                washingMachine.getDoor().send(new DoorControlMessage(DoorControlMessage.Activity.UNLOCK));
                state = State.STOPPED;
                washingMachine.setStatus("Wybierz program i wciśnij \"Start\"");
            }
        }
        if (msg instanceof PumpingFinishedMessage) {
            washingMachine.getDoor().send(new DoorControlMessage(DoorControlMessage.Activity.UNLOCK));
            state = State.STOPPED;
            washingMachine.setStatus("Wybierz program i wciśnij \"Start\"");
        }
    }

    private void processPausedState(Message msg) throws InterruptedException {
        if (msg instanceof ControlUnitMessage) {
            ControlUnitMessage cuMessage = (ControlUnitMessage) msg;
            if (cuMessage.getActivity() == ControlUnitMessage.Activity.STOP) {
                stopWashing();
            }
            if (cuMessage.getActivity() == ControlUnitMessage.Activity.START) {
                if (workingStates.contains(WorkingState.INIT_WASHING)) {
                    scheduleMessage(new InitFinishedMessage(), operationRemainingTime);
                    washingMachine.setStatus("Pobieranie proszku do prania");
                }
                if (workingStates.contains(WorkingState.PUMPING_INSIDE)) {
                    pumpController.send(new PumpControllerMessage(Pump.Direction.INSIDE, WorkingStateMessage.Activity.START));
                    washingMachine.setStatus("Pobieranie wody");
                }
                if (workingStates.contains(WorkingState.HEATING)) {
                    tempController.send(new TemperatureControllerMessage(currentProgram.getTemperature(), WorkingStateMessage.Activity.START));
                }
                if (workingStates.contains(WorkingState.WASHING)) {
                    washingController.send(new WashingControllerMessage(cradleMoves, WorkingStateMessage.Activity.START));
                    scheduleMessage(new WashingFinishedMessage(), operationRemainingTime);
                    washingMachine.setStatus(getStageStatus());
                }
                if (workingStates.contains(WorkingState.PUMPING_OUTSIDE)) {
                    pumpController.send(new PumpControllerMessage(Pump.Direction.OUTSIDE, WorkingStateMessage.Activity.START));
                    washingMachine.setStatus("Wypompowywanie");
                }
                if (workingStates.contains(WorkingState.SPINNING)) {
                    washingController.send(new WashingControllerMessage(cradleMoves, WorkingStateMessage.Activity.START));
                    scheduleMessage(new WashingFinishedMessage(), operationRemainingTime);
                    washingMachine.setStatus("Wirowanie");
                }
                if (workingStates.contains(WorkingState.WAITING_AFTER_WASHING)) {
                    scheduleMessage(new WaitingFinishedMessage(), operationRemainingTime);
                    washingMachine.setStatus("Zaczekaj na odblokowanie drzwi");
                }
                state = State.WORKING;
            }
        }
    }

    public ControlUnit(PumpController pumpController, TemperatureController tempController, WashingController washingController, WashingMachine washingMachine) {
        this.pumpController = pumpController;
        this.tempController = tempController;
        this.washingController = washingController;
        this.washingMachine = washingMachine;
        state = State.STOPPED;
        washingMachine.setStatus("Wybierz program i wciśnij \"Start\"");
        pumpController.setControlUnit(this);
    }

    @Override
    public void simulationStep() {
        try {
            Message msg = messageQueue.take();
            switch (state) {
                case STOPPED:
                    processStopedState(msg);
                    break;
                case STOPPING:
                    processStoppingState(msg);
                    break;
                case WORKING:
                    processWorkingState(msg);
                    break;
                case PAUSED:
                    processPausedState(msg);
                    break;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlUnit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public WashingStage getCurrentStage() {
        return currentStage;
    }
}
