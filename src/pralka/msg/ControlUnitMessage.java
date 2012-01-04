
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
    
    private boolean initialWashing;

    public ControlUnitMessage(Activity activity, Program chosenProgram, boolean initialWashing) {
        this.activity = activity;
        this.chosenProgram = chosenProgram;
        this.initialWashing = initialWashing;
    }


    public Activity getActivity() {
        return activity;
    }

    public Program getChosenProgram() {
        return chosenProgram;
    }

    public boolean doInitialWashing() {
        return initialWashing;
    }
    
    
}
