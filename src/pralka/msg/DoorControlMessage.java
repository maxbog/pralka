package pralka.msg;

public class DoorControlMessage extends Message {
    public static enum Activity {
        OPEN,
        CLOSE,
        LOCK,
        UNLOCK
    }
    
    private Activity activity;

    public DoorControlMessage(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }
    
    
    
}
