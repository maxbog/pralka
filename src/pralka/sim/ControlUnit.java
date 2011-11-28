/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pralka.sim;


public class ControlUnit {
    
   public enum Substate {
       IDLE,
       
       INIT_WASHING,
       WASHING,       
       RINSING,
       SPINNING
    }
    
    private Program currentProgram;
    private Substate currentSubstate;
  //  private Pralka pralka;
    
    private WashingMachine washingMachine;

    public ControlUnit(WashingMachine washingMachine) {
        this.washingMachine = washingMachine;
    }
    
    
    
    public void setNewProgram(Program p) {
        
        // operacje konieczne do zmiany programu
        
        currentProgram = p;
        
    }

    public void setNewSubstate(Substate s) {
        
        // operacje konieczne do zmiany etapu
        currentSubstate = s;
        
    }
    
    public void Run() {
        
        switch(currentSubstate) {
            case IDLE:
                            break;
                
            
            
            case WASHING:
                            break;
                
            case RINSING:
                            break;
                
            case SPINNING:
                            break;
                
                
           
        }
        
    }
    
    
}
