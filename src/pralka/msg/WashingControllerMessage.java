package pralka.msg;

public class WashingControllerMessage extends WorkingStateMessage {
    private boolean cradleMoves;

    public WashingControllerMessage(boolean cradleMoves, Activity activity) {
        super(activity);
        this.cradleMoves = cradleMoves;
    }

    public boolean doCradleMoves() {
        return cradleMoves;
    }
    
    
}
