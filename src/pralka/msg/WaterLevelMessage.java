package pralka.msg;

public class WaterLevelMessage extends Message {
    private boolean aboveHighLevel;
    private boolean aboveLowLevel;

    public WaterLevelMessage(boolean aboveHighLevel, boolean aboveLowLevel) {
        this.aboveHighLevel = aboveHighLevel;
        this.aboveLowLevel = aboveLowLevel;
    }

    public boolean isAboveHighLevel() {
        return aboveHighLevel;
    }

    public boolean isAboveLowLevel() {
        return aboveLowLevel;
    }
    
    
}
