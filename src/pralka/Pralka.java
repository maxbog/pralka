package pralka;

import javax.swing.UIManager;
import pralka.ui.MainFrame;

public class Pralka {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
        }
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }
}
