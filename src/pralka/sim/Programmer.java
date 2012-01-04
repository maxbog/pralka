package pralka.sim;

import com.jgoodies.binding.beans.Model;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.ControlUnitMessage;

public class Programmer extends Model {

    private static final Map<PredefinedProgram, Program> predefinedPrograms;
    private static final ArrayList<Integer> predefinedTemperatures;
    private static final ArrayList<Integer> predefinedSpeeds;
    private static final ArrayList<Integer> predefinedTimes;

    public static enum PredefinedProgram {

        PROGRAM_COTTON,
        PROGRAM_WOOL,
        PROGRAM_DELICATE,
        PROGRAM_FAST,
        PROGRAM_HANDWASH,
        PROGRAM_CUSTOM
    }

    static {
        predefinedPrograms = new EnumMap<PredefinedProgram, Program>(PredefinedProgram.class);
        predefinedPrograms.put(PredefinedProgram.PROGRAM_COTTON, new Program("Bawełna", 60, 800, 800, 1000, 50*60, 10*60, 20*60, false));
        predefinedPrograms.put(PredefinedProgram.PROGRAM_WOOL, new Program("Wełna", 40, 100, 100, 100, 60*60, 30*60, 50*60, true));
        predefinedPrograms.put(PredefinedProgram.PROGRAM_DELICATE, new Program("Delikatne", 30, 100, 100, 100, 40*60, 20*60, 20*60, false));
        predefinedPrograms.put(PredefinedProgram.PROGRAM_FAST, new Program("Szybki", 40, 800, 800, 1000, 20*60, 20*60, 10*60, false));
        predefinedPrograms.put(PredefinedProgram.PROGRAM_HANDWASH, new Program("Ręczne", 20, 100, 100, 1000, 20*60, 20*60, 0, true));

        predefinedTemperatures = new ArrayList<Integer>();
        predefinedTemperatures.add(30);
        predefinedTemperatures.add(40);
        predefinedTemperatures.add(60);
        predefinedTemperatures.add(90);
        
        predefinedSpeeds = new ArrayList<Integer>();
        predefinedSpeeds.add(100);
        predefinedSpeeds.add(300);
        predefinedSpeeds.add(500);
        predefinedSpeeds.add(800);
        
        predefinedTimes = new ArrayList<Integer>();
        predefinedTimes.add(20);
        predefinedTimes.add(30);
        predefinedTimes.add(40);
        predefinedTimes.add(50);
    }
    private WashingMachine washingMachine;

    public Programmer(WashingMachine washingMachine) {
        chosenProgram = PredefinedProgram.PROGRAM_COTTON;
        customProgram.copyFrom(predefinedPrograms.get(PredefinedProgram.PROGRAM_COTTON));
        this.washingMachine = washingMachine;
    }

    public void changeTemperature() {
        int oldTemp = customProgram.getTemperature();
        int currentTempIndex = predefinedTemperatures.indexOf(customProgram.getTemperature());
        customProgram.setTemperature(predefinedTemperatures.get((currentTempIndex + 1) % predefinedTemperatures.size()));
        firePropertyChange(PROPERTYNAME_TEMPERATURE, oldTemp, customProgram.getTemperature());
    }
    public static final String PROPERTYNAME_TEMPERATURE = "temperature";

    public int getTemperature() {
        return customProgram.getTemperature();
    }
    
    public void changeSpeed() {
        int oldSpeed = customProgram.getWashingSpeed();
        int currentTempIndex = predefinedSpeeds.indexOf(customProgram.getWashingSpeed());
        final Integer newWashingSpeed = predefinedSpeeds.get((currentTempIndex + 1) % predefinedSpeeds.size());
        customProgram.setWashingSpeed(newWashingSpeed);
        customProgram.setRinsingSpeed(newWashingSpeed);
        customProgram.setSpinningSpeed((int)(newWashingSpeed * 1.2));
        firePropertyChange(PROPERTYNAME_SPEED, oldSpeed, customProgram.getWashingSpeed());
    }
    public static final String PROPERTYNAME_SPEED = "speed";

    public int getSpeed() {
        return customProgram.getWashingSpeed();
    }
    
    public void changeTime() {
        int oldTime = customProgram.getWashingTime();
        int currentTempIndex = predefinedTimes.indexOf(customProgram.getWashingTime());
        final Integer newWashingTime = predefinedTimes.get((currentTempIndex + 1) % predefinedTimes.size());
        customProgram.setWashingTime(newWashingTime);
        customProgram.setRinsingTime(newWashingTime);
        customProgram.setSpinningTime((int)(newWashingTime*0.6));
        firePropertyChange(PROPERTYNAME_TIME, oldTime, customProgram.getWashingTime());
    }
    public static final String PROPERTYNAME_TIME = "time";

    public int getTime() {
        return customProgram.getWashingTime();
    }
    

    public void startWashing() {
        try {
            if (chosenProgram == PredefinedProgram.PROGRAM_CUSTOM) {
                washingMachine.getControlUnit().send(new ControlUnitMessage(ControlUnitMessage.Activity.START, customProgram, additionalWash));
            } else {
                washingMachine.getControlUnit().send(new ControlUnitMessage(ControlUnitMessage.Activity.START, predefinedPrograms.get(chosenProgram), additionalWash));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Programmer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopWashing() {
        try {
            washingMachine.getControlUnit().send(new ControlUnitMessage(ControlUnitMessage.Activity.STOP, null, false));
        } catch (InterruptedException ex) {
            Logger.getLogger(Programmer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pauseWashing() {
        try {
            washingMachine.getControlUnit().send(new ControlUnitMessage(ControlUnitMessage.Activity.PAUSE, null, false));
        } catch (InterruptedException ex) {
            Logger.getLogger(Programmer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Program customProgram = new Program();
    //<editor-fold defaultstate="collapsed" desc="Property - additionalWash">
    public static final String PROPERTYNAME_ADDITIONAL_WASH = "additionalWash";
    private boolean additionalWash;

    public boolean getAdditionalWash() {
        return additionalWash;
    }

    public void setAdditionalWash(boolean additionalWash) {
        if (additionalWash == this.additionalWash) {
            return;
        }
        boolean oldValue = this.additionalWash;
        this.additionalWash = additionalWash;
        firePropertyChange(PROPERTYNAME_ADDITIONAL_WASH, oldValue, additionalWash);
    }
    //</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="Property - cradleMoves">
    public static final String PROPERTYNAME_CRADLE_MOVES = "cradleMoves";

    public boolean getCradleMoves() {
        return customProgram.doCradleMoves();
    }

    public void setCradleMoves(boolean cradleMoves) {
        if (cradleMoves == customProgram.doCradleMoves()) {
            return;
        }
        boolean oldValue = customProgram.doCradleMoves();
        customProgram.setCradleMoves(cradleMoves);
        firePropertyChange(PROPERTYNAME_CRADLE_MOVES, oldValue, cradleMoves);
    }
    //</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="Property - chosenProgram">
    public static final String PROPERTYNAME_CHOSEN_PROGRAM = "chosenProgram";
    private PredefinedProgram chosenProgram;

    public PredefinedProgram getChosenProgram() {
        return chosenProgram;
    }

    public void setChosenProgram(PredefinedProgram chosenProgram) {
        if (chosenProgram == this.chosenProgram) {
            return;
        }
        PredefinedProgram oldValue = this.chosenProgram;
        this.chosenProgram = chosenProgram;
        if(chosenProgram != PredefinedProgram.PROGRAM_CUSTOM) {
            updateDisplayedValues(chosenProgram);
        }
        firePropertyChange(PROPERTYNAME_CHOSEN_PROGRAM, oldValue, chosenProgram);
    }
    //</editor-fold>

    private void updateDisplayedValues(PredefinedProgram chosenProgram) {
        Program oldProgram = new Program();
        oldProgram.copyFrom(customProgram);
        customProgram.copyFrom(predefinedPrograms.get(chosenProgram));
        firePropertyChange(PROPERTYNAME_CRADLE_MOVES, oldProgram.doCradleMoves(), customProgram.doCradleMoves());
        firePropertyChange(PROPERTYNAME_SPEED, oldProgram.getWashingSpeed(), customProgram.getWashingSpeed());
        firePropertyChange(PROPERTYNAME_TEMPERATURE, oldProgram.getTemperature(), customProgram.getTemperature());
        firePropertyChange(PROPERTYNAME_TIME, oldProgram.getWashingTime(), customProgram.getWashingTime());
    }
}
