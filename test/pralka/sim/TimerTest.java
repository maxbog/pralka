package pralka.sim;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {
    @Test
    public void testStartPausedWithZeroTimeAndNormalScale() {
        Timer t = new Timer();
        assertEquals(false, t.isRunning());
        assertEquals(0, t.getCurrentTime(), 0.0001);
        assertEquals(1.0, t.getTimeScale(), 0.0001);
    }
    
    @Test
    public void testNormalTime() throws InterruptedException {
        Timer t = new Timer();
        t.resume();
        double time1 = t.getCurrentTime();
        Thread.sleep(50);
        double time2 = t.getCurrentTime();
        assertTrue("time1(" + Double.toString(time1) + ") >= time2("+ Double.toString(time2) + ")",time2 > time1);
    }
    
    @Test
    public void testPause() throws InterruptedException {
        Timer t = new Timer();
        t.resume();
        double time1 = t.getCurrentTime();
        Thread.sleep(50);
        t.pause();
        double time2 = t.getCurrentTime();
        Thread.sleep(50);
        double time3 = t.getCurrentTime();
        t.resume();
        Thread.sleep(50);
        double time4 = t.getCurrentTime();
        
        assertTrue("time1(" + Double.toString(time1) + ") >= time2("+ Double.toString(time2) + ")",time2 > time1);
        assertEquals(time2, time3,0.0001);
        assertTrue("time3(" + Double.toString(time3) + ") >= time4("+ Double.toString(time4) + ")",time4 > time3);      
    }
    
    @Test
    public void testTimeScale() throws InterruptedException {
        Timer t = new Timer();
        t.resume();
        double time1 = t.getCurrentTime();
        Thread.sleep(50);
        double time2 = t.getCurrentTime();
        t.setTimeScale(10.0);
        double time3 = t.getCurrentTime();
        Thread.sleep(50);
        double time4 = t.getCurrentTime();
        
        assertTrue("time1(" + Double.toString(time1) + ") >= time2("+ Double.toString(time2) + ")",time2 > time1);
        assertTrue("time3(" + Double.toString(time3) + ") >= time4("+ Double.toString(time4) + ")",time4 > time3);  
        assertTrue("(time1-time2)*5(" + Double.toString((time1-time2)*5) + ") >= (time3-time4)("+ Double.toString((time3-time4)) + ")",(time1-time2)*5 < (time3-time4));    
    }
    
    @Test
    public void testReset() throws InterruptedException {
        Timer t = new Timer();
        t.resume();
        double time1 = t.getCurrentTime();
        Thread.sleep(50);
        double time2 = t.getCurrentTime();
        t.pause();
        boolean running = t.isRunning();
        t.reset();        
        
        assertTrue("time1(" + Double.toString(time1) + ") >= time2("+ Double.toString(time2) + ")",time2 > time1);
        assertEquals(0, t.getCurrentTime(),0.0001);
        assertEquals(false, running);
        assertEquals(false, t.isRunning());
    }
}
