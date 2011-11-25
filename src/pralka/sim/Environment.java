package pralka.sim;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Environment extends SimulationThread {
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
            if (waterLevel + delta <= WATER_HIGH_LEVEL) {
                waterLevel += delta;
            }
        } finally {
            waterLevelLock.writeLock().unlock();
        }
    }

    public void decreaseWaterLevel(double delta) {
        waterLevelLock.writeLock().lock();
        try {
            if (waterLevel - delta >= WATER_LOW_LEVEL) {
                waterLevel -= delta;
            }
        } finally {
            waterLevelLock.writeLock().unlock();
        }
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Temperatura wody">
    private double waterTemperature;
    private ReadWriteLock waterTemperatureLock = new ReentrantReadWriteLock();
    private double heatingPower;
    private static final double TIME_CONSTANT = 1.;
    // temperatura pokojowa
    private static final double COOLING_POWER = 20;

    public double getWaterTemperature() {
        waterTemperatureLock.readLock().lock();
        try {
            return waterTemperature;
        } finally {
            waterTemperatureLock.readLock().unlock();
        }
    }

    public void setHeatingPower(double heatingPower) {
        waterTemperatureLock.writeLock().lock();
        try {
            this.heatingPower = heatingPower;
        } finally {
            waterTemperatureLock.writeLock().unlock();
        }
    }

    private void updateWaterTemperature() {
        waterTemperatureLock.writeLock().lock();
        try {
            //aproksymacja obiektem inercyjnym pierwszego rzędu :)
            waterTemperature += (getTimeDelta() / TIME_CONSTANT) * (heatingPower + COOLING_POWER - waterTemperature);
        } finally {
            waterTemperatureLock.writeLock().unlock();
        }
    }
    //</editor-fold>      
    private User user;

    @Override
    public void run() {
        while (simulationRunning()) {
            updateWaterTemperature();
        }
    }
}
