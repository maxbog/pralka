package pralka.sim;

import com.jgoodies.binding.beans.Model;
import java.util.EnumMap;
import java.util.Map;

public class Programmer extends Model {
    private static final Map<PredefinedProgram, Program> predefinedPrograms;
    
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
    }
    
    public Programmer() {
        chosenProgram = PredefinedProgram.PROGRAM_COTTON;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Property - chosenProgram">
    public static final String PROPERTYNAME_CHOSEN_PROGRAM = "chosenProgram";
    private PredefinedProgram chosenProgram;

    public PredefinedProgram getChosenProgram() {
        return chosenProgram;
    }

    public void setChosenProgram(PredefinedProgram chosenProgram) {
        if(chosenProgram == this.chosenProgram)
            return;
        PredefinedProgram oldValue = this.chosenProgram;
        this.chosenProgram = chosenProgram;
        firePropertyChange(PROPERTYNAME_CHOSEN_PROGRAM, oldValue, chosenProgram);        
    }
    //</editor-fold>
}
