package pralka.msg;

import pralka.sim.SimulationThread;

public class GetMeasurementMessage extends Message {
    private SimulationThread returnThread;

    public GetMeasurementMessage(SimulationThread returnThread) {
        this.returnThread = returnThread;
    }

    public SimulationThread getReturnThread() {
        return returnThread;
    }
    
    
}
