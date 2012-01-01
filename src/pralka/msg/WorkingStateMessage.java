package pralka.msg;

public class WorkingStateMessage extends Message {
    public static enum Activity {
        START,
        STOP,
    }
    
    Activity activity;

    public WorkingStateMessage(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }
}
