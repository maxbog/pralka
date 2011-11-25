package pralka.sim;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Timer {
    private static final Logger logger = Logger.getLogger(Timer.class.getName());
    
    private boolean paused = true;
    private long elapsedTime = 0;
    private long lastTimeNs = System.nanoTime();
    private double timeScale = 1.0;
    
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private Lock readLock = rwLock.readLock();
    private Lock writeLock = rwLock.writeLock();
    private Condition resume = writeLock.newCondition();
    
    public void waitWhilePaused() {
        if(!paused)
            return;
        // musi być lock do zapisu, żeby czekać na wybudzenie
        writeLock.lock();   
        try {        
            while(paused) {
                try {
                    resume.await();
                } catch (InterruptedException ex) {
                    logger.log(Level.INFO, "Przerwano w czasie pauzy", ex);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }      
    
    public void pause() {
        if(paused)
            return;
        writeLock.lock();   
        try {
            if(paused)
                return;
            paused = true;
            updateElapsedTime();
        } finally {
            writeLock.unlock();
        }
    }
    
    public void resume() {
        if(!paused)
            return;
        writeLock.lock();   
        try {
            if(!paused)
                return;
            paused = false;
            lastTimeNs = System.nanoTime();
            resume.signalAll();
        } finally {
            writeLock.unlock();
        }        
    }
    
    public void reset() {
        writeLock.lock();   
        try {
            elapsedTime = 0;
            lastTimeNs = System.nanoTime();
        } finally {
            writeLock.unlock();
        }
    }
    
    public int getCurrentTime() {
        readLock.lock();   
        try {
            if(paused)
                return (int) (elapsedTime/1000);
            return (int) ((System.nanoTime() - lastTimeNs + elapsedTime)/1000);
        } finally {
            readLock.unlock();
        }
    }
    
    public boolean isRunning() {
        readLock.lock();   
        try {
            return !paused;
        } finally {
            readLock.unlock();
        }
    }

    public double getTimeScale() {
        readLock.lock();   
        try {
            return timeScale;
        } finally {
            readLock.unlock();
        }
    }

    public void setTimeScale(double timeScale) {
        writeLock.lock();   
        try {
            updateElapsedTime();
            this.timeScale = timeScale;
        } finally {
            writeLock.unlock();
        }
    }

    private void updateElapsedTime() {
        final long nanoTime = System.nanoTime();
        elapsedTime += (nanoTime - lastTimeNs) * timeScale;
        lastTimeNs = nanoTime;
    }
    
    
}
