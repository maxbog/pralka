package pralka.sim;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import pralka.msg.Message;

public class Timer extends Thread {

    synchronized void clearScheduledMessages(SimulationThread destination) {
        ArrayList<ScheduledMessage> toRemove = new ArrayList<ScheduledMessage>();
        for(ScheduledMessage msg : scheduledMessages) {
            if(msg.getDestination() == destination) {
                toRemove.add(msg);
            }
        }
        scheduledMessages.removeAll(toRemove);
    }

    public static class ScheduledMessage {

        Message message;
        SimulationThread destination;
        double time;

        public SimulationThread getDestination() {
            return destination;
        }

        public Message getMessage() {
            return message;
        }

        public double getTime() {
            return time;
        }

        public ScheduledMessage(Message message, SimulationThread destination, double time) {
            this.message = message;
            this.destination = destination;
            this.time = time;
        }
    }
    private static final Logger logger = Logger.getLogger(Timer.class.getName());
    private boolean paused = true;
    private long elapsedTime = 0;
    private long lastTimeNs = System.nanoTime();
    private double timeScale = 1.0;
    private ReadWriteLock timerLock = new ReentrantReadWriteLock();
    private Condition resume = timerLock.writeLock().newCondition();
    private PriorityQueue<ScheduledMessage> scheduledMessages;

    public Timer() {
        scheduledMessages = new PriorityQueue<ScheduledMessage>(100, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                if (!(o1 instanceof ScheduledMessage) || !(o2 instanceof ScheduledMessage)) {
                    throw new ClassCastException();
                }
                ScheduledMessage sm1 = (ScheduledMessage) o1;
                ScheduledMessage sm2 = (ScheduledMessage) o2;
                return (int) (sm1.getTime() - sm2.getTime());
            }
        });
    }

    public void waitWhilePaused() {
        if (!paused) {
            return;
        }
        // musi być lock do zapisu, żeby czekać na wybudzenie
        timerLock.writeLock().lock();
        try {
            while (paused) {
                try {
                    resume.await();
                } catch (InterruptedException ex) {
                    logger.log(Level.INFO, "Przerwano w czasie pauzy", ex);
                }
            }
        } finally {
            timerLock.writeLock().unlock();
        }
    }

    public void pause() {
        if (paused) {
            return;
        }
        timerLock.writeLock().lock();
        try {
            if (paused) {
                return;
            }
            paused = true;
            updateElapsedTime();
        } finally {
            timerLock.writeLock().unlock();
        }
    }

    public void resumeTimer() {
        if (!paused) {
            return;
        }
        timerLock.writeLock().lock();
        try {
            if (!paused) {
                return;
            }
            paused = false;
            lastTimeNs = System.nanoTime();
            resume.signalAll();
        } finally {
            timerLock.writeLock().unlock();
        }
    }

    public void reset() {
        timerLock.writeLock().lock();
        try {
            elapsedTime = 0;
            lastTimeNs = System.nanoTime();
        } finally {
            timerLock.writeLock().unlock();
        }
    }

    public double getCurrentTime() {
        timerLock.readLock().lock();
        try {
            if (paused) {
                return elapsedTime / (1000. * 1000. * 1000.);
            }
            return (timeScale*(System.nanoTime() - lastTimeNs) + elapsedTime) / (1000. * 1000. * 1000.);
        } finally {
            timerLock.readLock().unlock();
        }
    }

    public boolean isRunning() {
        timerLock.readLock().lock();
        try {
            return !paused;
        } finally {
            timerLock.readLock().unlock();
        }
    }

    public double getTimeScale() {
        timerLock.readLock().lock();
        try {
            return timeScale;
        } finally {
            timerLock.readLock().unlock();
        }
    }

    public void setTimeScale(double timeScale) {
        timerLock.writeLock().lock();
        try {
            updateElapsedTime();
            this.timeScale = timeScale;
        } finally {
            timerLock.writeLock().unlock();
        }
    }

    private void updateElapsedTime() {
        final long nanoTime = System.nanoTime();
        elapsedTime += (nanoTime - lastTimeNs) * timeScale;
        lastTimeNs = nanoTime;
    }

    public synchronized void scheduleMessage(Message msg, SimulationThread destination, double time) {
        scheduledMessages.add(new ScheduledMessage(msg, destination, getCurrentTime() + time));
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    ScheduledMessage msg = scheduledMessages.poll();
                    if (msg != null) {
                        if (msg.getTime() <= getCurrentTime()) {
                            msg.getDestination().send(msg.getMessage());
                        } else {
                            scheduledMessages.offer(msg);
                        }
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
