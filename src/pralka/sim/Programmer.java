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
        predefinedPrograms.put(PredefinedProgram.PROGRAM_COTTON, new Program("Bawełna", 30, 800, 800, 1000, 30, 20, 10, false, false));

        predefinedTemperatures = new ArrayList<Integer>();
        predefinedTemperatures.add(30);
        predefinedTemperatures.add(40);
        predefinedTemperatures.add(60);
        predefinedTemperatures.add(90);
    }
    private WashingMachine washingMachine;

    public Programmer(WashingMachine washingMachine) {
        chosenProgram = PredefinedProgram.PROGRAM_COTTON;
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

    public void startWashing() {
        try {
            if (chosenProgram == PredefinedProgram.PROGRAM_CUSTOM) {
                washingMachine.getControlUnit().send(new ControlUnitMessage(ControlUnitMessage.Activity.START, customProgram));
            } else {
                washingMachine.getControlUnit().send(new ControlUnitMessage(ControlUnitMessage.Activity.START, predefinedPrograms.get(chosenProgram)));
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Programmer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopWashing() {
        try {
            washingMachine.getControlUnit().send(new ControlUnitMessage(ControlUnitMessage.Activity.STOP, null));
        } catch (InterruptedException ex) {
            Logger.getLogger(Programmer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pauseWashing() {
        try {
            washingMachine.getControlUnit().send(new ControlUnitMessage(ControlUnitMessage.Activity.PAUSE, null));
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
        firePropertyChange(PROPERTYNAME_CHOSEN_PROGRAM, oldValue, chosenProgram);
    }
    //</editor-fold>
}
