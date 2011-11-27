/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pralka.sim;

public class Door {
    
    private static enum LockState {
        LOCKED,
        UNLOCKED
    }
    
    private static enum State {
        OPENED,
        CLOSED
    }
    
    private State state;
    private LockState lockState;
    
    public boolean opened() {
        return ( state == State.OPENED );
    }
       
    public boolean open() {
        if (lockState == LockState.UNLOCKED) {
            state = State.OPENED;
            return true;
        }
        else
            return false;
    }    
     
    public void close() {
         state = State.CLOSED;
     }
     
    public void lock() {
         lockState = LockState.LOCKED;
     }
     
    public void unlock() {
         lockState = LockState.UNLOCKED;
     }
}
