/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pralka.sim;

public class Program {
    
    private int temperature;
    private int washingSpeed;
    private int rinsingSpeed;
    private int spinningSpeed;
    private int washingTime;
    private int rinsingTime;
    private int spinningTime;       
    private boolean cradleMoves;     
    private String name;
    
    public Program() {
        temperature = 60;
        washingSpeed = 800;
        rinsingSpeed = 800;
        spinningSpeed = 1000;
        washingTime = 30;
        rinsingTime = 20;
        spinningTime = 10;
        cradleMoves = false;
        name = "Bawelna60";
    }
    
    public Program(String name, int temperature, int washingSpeed, int rinsingSpeed, int spinningSpeed, int washingTime, int rinsingTime, int spinningTime, boolean cradleMoves) {
        this.name = name;
        this.rinsingSpeed = rinsingSpeed;
        this.rinsingTime = rinsingTime;
        this.spinningSpeed = spinningSpeed;
        this.spinningTime = spinningTime;
        this.temperature = temperature;
        this.washingSpeed = washingSpeed;
        this.washingTime = washingTime;
        this.cradleMoves = cradleMoves;
     }
    
    public void copyFrom(Program other) {
        this.name = other.name;
        this.rinsingSpeed = other.rinsingSpeed;
        this.rinsingTime = other.rinsingTime;
        this.spinningSpeed = other.spinningSpeed;
        this.spinningTime = other.spinningTime;
        this.temperature = other.temperature;
        this.washingSpeed = other.washingSpeed;
        this.washingTime = other.washingTime;
        this.cradleMoves = other.cradleMoves;
    }
    
    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    
   public int getWashingSpeed() {
        return washingSpeed;
    }
   
   public int getRinsingSpeed() {
        return rinsingSpeed;
    }
   
   public int getSpinningSpeed() {
       return spinningSpeed;
   }

   public int getWashingTime() {
       return washingTime;
   }
   
   public int getRinsingTime() {
        return rinsingTime;
    }
   
   public int getSpinningTime() {
       return spinningTime;   
   }
       
   public boolean doCradleMoves() {
       return cradleMoves;
   }
   
   public String getName() {
       return name;
   }

    void setCradleMoves(boolean cradleMoves) {
        this.cradleMoves = cradleMoves;
    }

    void setWashingSpeed(int washingSpeed) {
        this.washingSpeed = washingSpeed;
    }

    public void setRinsingSpeed(int rinsingSpeed) {
        this.rinsingSpeed = rinsingSpeed;
    }

    public void setRinsingTime(int rinsingTime) {
        this.rinsingTime = rinsingTime;
    }

    public void setSpinningSpeed(int spinningSpeed) {
        this.spinningSpeed = spinningSpeed;
    }

    public void setSpinningTime(int spinningTime) {
        this.spinningTime = spinningTime;
    }

    public void setWashingTime(int washingTime) {
        this.washingTime = washingTime;
    }
    
    
    
}
