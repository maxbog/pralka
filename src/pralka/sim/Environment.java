package pralka.sim;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Environment {
    //<editor-fold defaultstate="collapsed" desc="Poziom wody">
    private static final double WATER_LOW_LEVEL = 0;
    private static final double WATER_HIGH_LEVEL = 100;
    private double waterLevel;
    private ReadWriteLock waterLevelLock = new ReentrantReadWriteLock();
    
    public double getWaterLevel() {
        waterLevelLock.readLock().lock();
        try {
            return waterLevel;
        } finally {
            waterLevelLock.readLock().unlock();
        }
    }
    
    public boolean waterLevelTooLow() {
        waterLevelLock.readLock().lock();
        try {
            return Math.abs(waterLevel - WATER_LOW_LEVEL) <= 0.01;
        } finally {
            waterLevelLock.readLock().unlock();
        }
    } 
    
    public void increaseWaterLevel(double delta) {
        waterLevelLock.writeLock().lock();
        try {
            if(waterLevel + delta <= WATER_HIGH_LEVEL)
                waterLevel += delta;
        } finally {
            waterLevelLock.writeLock().unlock();
        }
    }
    
    public void decreaseWaterLevel(double delta) {
        waterLevelLock.writeLock().lock();
        try {
            if(waterLevel - delta >= WATER_LOW_LEVEL)
                waterLevel -= delta;
        } finally {
            waterLevelLock.writeLock().unlock();
        }
    }
    //</editor-fold>
        
    private int waterTemperature;
    private User user;

    public int getWaterTemperature() {
        return waterTemperature;
    }
}
