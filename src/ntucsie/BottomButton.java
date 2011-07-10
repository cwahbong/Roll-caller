package ntucsie;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

/**
 * Button(s) that is placed in the south panel.
 * @author cw.ahbong
 * @see     javax.swing.JButton
 */
public class BottomButton extends JButton {
    private static final long serialVersionUID = 1L;

    /**
     * @param string    The text of this button.
     */
    public BottomButton(String string) {
        setText(string);
        setPreferredSize(new Dimension(100,25));
        setBackground(new Color(0,150,0));
        setForeground(Color.white);
        setFocusable(false);
    }
}
