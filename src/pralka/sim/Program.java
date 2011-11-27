/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pralka.sim;

public class Program {
    
    private Integer temperature;
    private Integer washingSpeed;
    private Integer rinsingSpeed;
    private Integer spinningSpeed;
    private Integer washingTime;
    private Integer rinsingTime;
    private Integer spinningTime;
    private boolean initWashing;       
    private boolean woolCradle;     
    private String name;
    
    public Program() {
        temperature = 30;
        washingSpeed = 800;
        rinsingSpeed = 800;
        spinningSpeed = 1000;
        washingTime = 30;
        rinsingTime = 20;
        spinningTime = 10;
        initWashing = false;
        woolCradle = false;
        name = "Bawelna30";
    }
    
    public Program(Integer temp, Integer washingSpeed, Integer rinsingSpeed, Integer spinningSpeed, Integer washingTime, Integer rinsingTime,Integer spinningTime,boolean initWashing,boolean woolCradle,String name) {
        this.initWashing = initWashing;
        this.name = name;
        this.rinsingSpeed = rinsingSpeed;
        this.rinsingTime = rinsingTime;
        this.spinningSpeed = spinningSpeed;
        this.spinningTime = spinningTime;
        this.temperature = temp;
        this.washingSpeed = washingSpeed;
        this.washingTime = washingTime;
        this.woolCradle = woolCradle;
     }
    
    public Integer getTemperature() {
        return temperature;
    }
    
   public Integer getWashingSpeed() {
        return washingSpeed;
    }
   
   public Integer getRinsingSpeed() {
        return rinsingSpeed;
    }
   
   public Integer getSpinningSpeed() {
       return spinningSpeed;
   }

   public Integer getWashingTime() {
       return washingTime;
   }
   
   public Integer getRinsingTime() {
        return rinsingTime;
    }
   
   public Integer getSpinningTime() {
       return spinningTime;   
   }
       
   public boolean isWoolCradle() {
       return woolCradle;
   }
   
   public boolean isInitWashing() {
       return initWashing;
   }
   
   public String getName() {
       return name;
   }
    
}
