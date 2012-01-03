
package pralka.msg;

import pralka.sim.Program;

public class ControlUnitMessage extends Message {
    public static enum Activity {
        START,
        STOP,
        PAUSE
    }
    
    private Activity activity;
    
    private Program chosenProgram;

    public ControlUnitMessage(Activity activity, Program chosenProgram) {
        this.activity = activity;
        this.chosenProgram = chosenProgram;
    }

    public Activity getActivity() {
        return activity;
    }

    public Program getChosenProgram() {
        return chosenProgram;
    }
}
