package pralka.sim;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.Message;
import pralka.msg.PumpControlMessage;

public class Pump extends SimulationThread {

    private static enum State {

        STOPPED,
        PUMPING_INSIDE,
        PUMPING_OUTSIDE
    }

    public static enum Direction {

        OUTSIDE,
        INSIDE
    }
    private Environment environment;
    private State state;
    private static final double litersPerSecond = 1.;

    public Pump(Environment environment) {
        this.environment = environment;
        this.state = State.STOPPED;
    }

    public synchronized void startPumping(Direction direction) {
        switch (state) {
            case STOPPED:
                if (direction == Direction.INSIDE) {
                    state = State.PUMPING_INSIDE;
                } else {
                    state = State.PUMPING_OUTSIDE;
                }
                break;
        }
    }

    public synchronized void stopPumping() {
        switch (state) {
            case PUMPING_INSIDE:
                state = State.STOPPED;
                break;
            case PUMPING_OUTSIDE:
                state = State.STOPPED;
                break;
        }
    }

    @Override
    public void run() {
        while (simulationRunning()) {
            synchronized (this) {
                switch (state) {
                    case PUMPING_INSIDE:
                        environment.increaseWaterLevel(litersPerSecond * getTimeDelta());
                    case PUMPING_OUTSIDE:
                        environment.decreaseWaterLevel(litersPerSecond * getTimeDelta());
                }
            }
        }
    }

    @Override
    protected void simulationStep() {
        try {
            Message msg = messageQueue.poll(0, TimeUnit.NANOSECONDS);
            if (msg instanceof PumpControlMessage) {
                handlePumpControlMessage((PumpControlMessage)msg);
            } else {
                switch (state) {
                    case PUMPING_INSIDE:
                        environment.increaseWaterLevel(litersPerSecond * getTimeDelta());
                    case PUMPING_OUTSIDE:
                        environment.decreaseWaterLevel(litersPerSecond * getTimeDelta());
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Pump.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handlePumpControlMessage(PumpControlMessage msg) {
        if (msg.getActivity() == PumpControlMessage.Activity.STOP) {
            switch (state) {
                case PUMPING_INSIDE:
                    state = State.STOPPED;
                    break;
                case PUMPING_OUTSIDE:
                    state = State.STOPPED;
                    break;
            }
        } else if (msg.getActivity() == PumpControlMessage.Activity.START && state == State.STOPPED) {
            switch (msg.getDirection()) {
                case INSIDE:
                    state = State.PUMPING_INSIDE;
                    break;
                case OUTSIDE:
                    state = State.PUMPING_OUTSIDE;
                    break;
            }
        }
    }
}
