package ntucsie;

import javax.swing.SwingUtilities;

/**
 * The entry point.
 * @author  cw.ahbong
 * @see     RollCallUi
 */
public class RollCall {
    /**
     * The entry point.
     * @param args      No effect.
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RollCallUi();
            }
        });
    }
}