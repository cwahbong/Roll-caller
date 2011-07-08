package ntucsie;

import java.io.FileNotFoundException;
import javax.swing.SwingUtilities;

public class RollCall {
    public static void main(String args[]) throws FileNotFoundException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RollCallUi();
            }
        });
    }
}