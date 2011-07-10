package ntucsie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>
 * The main GUI of Roll-caller. The frame is split into three part:
 * center part, east part, and south part. Each part is a JPanel.
 * </p>
 * 
 * <p>
 * The center part shows the animation.
 * The east part only contains a "play" button. When press this button,
 * the center part starts the animation and shows the name of the next
 * lucky man.
 * The south part contains a JSlider and some BottomButtons. 
 * </p>
 * 
 * @author cw.ahbong
 * @author NTUCS-MSLAB
 * @see     javax.swing.JPanel
 * @see     javax.swing.JSlider
 * @see     BottomButton
 */
public class RollCallUi extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private static final int spin_area_left = 30;
    private static final int spin_area_top = 50;
    private static final int spin_area_edge = 5;
    private static final int spin_area_padding = 10;
    private static final int spin_area_corner_arc = 10;
    private static final int spin_area_width = 110;
    private static final int spin_area_height = 260;
    
    private static final int text_area_left = spin_area_left + 10;;
    private static final int text_area_top = spin_area_top + 10;
    private static final int text_area_width = spin_area_width - 10;
    private static final int text_area_height = spin_area_height + 100;
    
    private String _luckyManName;
    private int _rollSpeed = 60;
    
    private RollCaller _rollCaller;
    private RollCallFileSet _fileset = new RollCallFileSet(new File("student.txt"));

    /*
     * Panel(s)
     */
    private JPanel _centerPanel;
    private JPanel _eastPanel;
    private JPanel _southPanel;

    /**
     * Getter.
     * @return  The center part panel.
     */
    public JPanel getCenterPanel() {
        if(_centerPanel==null) {
            _centerPanel = new JPanel(){
                private static final long serialVersionUID = 1L;
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(new Color(205,127,50));
                    for(int i=0; i<3; i++) {
                        add(getRollTextArea(i));
                        g.drawRoundRect(spin_area_left + i*(spin_area_width+spin_area_padding),
                                        spin_area_top,
                                        spin_area_width, spin_area_height,
                                        spin_area_corner_arc, spin_area_corner_arc);
                        g.fillRoundRect(spin_area_left + spin_area_edge + i*(spin_area_width+spin_area_padding),
                                        spin_area_top + spin_area_edge,
                                        spin_area_width - spin_area_edge*2, spin_area_height - spin_area_edge*2,
                                        spin_area_corner_arc, spin_area_corner_arc);
                    }
                }
            };
            // This construction should be moved to somewhere.
            getSpinRollTimer();
            //
            _centerPanel.setBackground(Color.black);
            _centerPanel.setLayout(null);
        }
        return _centerPanel;
    }

    /**
     * Getter.
     * @return  The east part panel.
     */
    public JPanel getEastPanel() {
        if(_eastPanel==null) {
            _eastPanel = new JPanel();
            _eastPanel.setLayout(new BorderLayout());
            _eastPanel.add(getPlayButton(), BorderLayout.CENTER);
        }
        return _eastPanel;
    }

    /**
     * Getter.
     * @return  The south part panel.
     */
    public JPanel getSouthPanel() {
        if(_southPanel==null) {
            _southPanel = new JPanel();
            _southPanel.setBackground(new Color(220,236,220));
            _southPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
            _southPanel.add(getSpeedSlider());
            _southPanel.add(getAbsentButton());
            _southPanel.add(getAboutButton());
        }
        return _southPanel;
    }
    
    /*
     * Component(s) in center panel.
     */
    private JTextArea[] _rollTextArea;
    private Timer _spinRollTimer;
    
    public JTextArea getRollTextArea(int order) {
        if(_rollTextArea==null) {
            _rollTextArea = new JTextArea[3];
        }
        if(_rollTextArea[order]==null) {
            _rollTextArea[order] = new JTextArea();
            _rollTextArea[order].setBounds(35+order*(text_area_width+20),0,text_area_width,text_area_height);
            // DFKai-sb is 標楷體, English font name is faster.
            _rollTextArea[order].setFont(new Font("DFKai-sb",Font.PLAIN,100)); 
            _rollTextArea[order].setForeground(Color.black);
            _rollTextArea[order].setOpaque(false);
            _rollTextArea[order].setFocusable(false);
        }
        return _rollTextArea[order];
    }
    
    public Timer getSpinRollTimer() {
        if(_spinRollTimer==null) {
            _spinRollTimer = new Timer(100-_rollSpeed, null);
            _spinRollTimer.setInitialDelay(0);
            _spinRollTimer.addActionListener(new RollAction(0));
            _spinRollTimer.addActionListener(new RollAction(1));
            _spinRollTimer.addActionListener(new RollAction(2));
        }
        return _spinRollTimer;
    }
    
    /*
     * Component(s) in east panel.
     */
    private JButton _playButton;
    
    /**
     * Getter.
     * @return  The "play" button.
     */
    public JButton getPlayButton() {
        if(_playButton==null) {
            _playButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("play.png")));
            _playButton.setFocusable(false);
            _playButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(!getSpinRollTimer().isRunning()) {
                        try {
                            _luckyManName = getRollCaller().rollCall();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        getSpinRollTimer().start();
                    }
                }
            });
        }
        return _playButton;
    }
    
    /*
     * Component(s) in south panel.
     */
    private JButton _aboutButton;
    private JButton _absentButton;
    private JSlider _speedSlider;
    
    public JButton getAboutButton() {
        if(_aboutButton==null) {
            _aboutButton = new BottomButton("About");
            _aboutButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null,
                            "Roll-caller v1.1\nBy NTUCS-MSLAB, modified by cw.ahbong.",
                            "",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
        return _aboutButton;
    }

    public JButton getAbsentButton() {
        if(_absentButton==null) {
            _absentButton = new BottomButton("Absent");
            _absentButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null,
                            "This function (hasn't been)/(shouldn't be)/(won't be) implemented.",
                            "",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
        return _absentButton;
    }

    public JSlider getSpeedSlider() {
        if(_speedSlider==null) {
            _speedSlider = new JSlider(SwingConstants.HORIZONTAL, 25, 95, 60);
            Dictionary<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
            labels.put(_speedSlider.getMinimum(), new JLabel("Slow"));
            labels.put(_speedSlider.getMaximum(), new JLabel("Fast"));
            _speedSlider.setLabelTable(labels);
            _speedSlider.setPaintLabels(true);
            _speedSlider.setOpaque(false);
            _speedSlider.setPreferredSize(new Dimension(150,50));
            _speedSlider.addChangeListener(new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent e) {
                    _rollSpeed = _speedSlider.getValue();
                    getSpinRollTimer().setDelay(100-_rollSpeed);
                }
            });
        }
        return _speedSlider;
    }

    public RollCaller getRollCaller() {
        if(_rollCaller==null) {
            _rollCaller = new RollCaller(_fileset);
        }
        return _rollCaller;
    }
    
    public RollCallUi() {
        super("Roll Call");
        setLocation(100, 100);
        setSize(600,435);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        add(getCenterPanel(), BorderLayout.CENTER);
        add(getEastPanel(), BorderLayout.EAST);
        add(getSouthPanel(), BorderLayout.SOUTH);
        validate();
        System.err.println("Main ui inited");
    }
    
    private class RollAction implements ActionListener {
        
        private static final int _rollSpeed = 30;
        
        private int _order;
        private int _count;
        private List<String> _text = new ArrayList<String>();
        private Random _random = new Random();
        
        public RollAction(int order) {
            _order = order;
            _count = 0;
            if(getRollCaller().getTotalStudentNum()>2) {
                for(int i=0; i<3; i++) {
                    _text.add(getRollCaller().getNameList().get(_random.nextInt(
                              getRollCaller().getNameList().size())).substring(_order, _order+1));
                }
            }
            else {
                switch(order) {
                    case 0:
                        _text.add("人");
                        _text.add("好");
                        _text.add("少");
                        break;
                    case 1:
                        _text.add("好");
                        _text.add("少");
                        _text.add("人");
                        break;
                    case 2:
                        _text.add("少");
                        _text.add("人");
                        _text.add("好");
                        break;
                }
            }
            getRollTextArea(_order).setText(_text.get(2)+"\n"+_text.get(1)+"\n"+_text.get(0));
        }
        
        private void nextSpin() {
            int left = getRollTextArea(_order).getX();
            int top = getRollTextArea(_order).getY();
            ++_count;
            if(_count <= _rollSpeed * _order) {
                getRollTextArea(_order).setLocation(left, top+20);
            }
            else if(_count <= _rollSpeed * (_order+1)) {
                getRollTextArea(_order).setLocation(left, top+10);
            }
            else if(_count <= _rollSpeed * (_order+2)) {
                getRollTextArea(_order).setLocation(left, top+2);
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            nextSpin();
            if(getRollTextArea(_order).getY() == text_area_top) {
                if(_count == _rollSpeed * (_order+1) - text_area_height/_rollSpeed) {
                //if(/*the lucky man name should appear*/) {
                    _text.add(_luckyManName.substring(_order,_order+1));
                }
                else {
                    _text.add(getRollCaller().getNameList().get(_random.nextInt(getRollCaller().getNameList().size())).substring(_order,_order+1));
                }
                _text.remove(0);
                getRollTextArea(_order).setLocation(getRollTextArea(_order).getX(),-60);
                getRollTextArea(_order).setText(_text.get(2)+"\n"+_text.get(1)+"\n"+_text.get(0));
            }
            if(_count == _rollSpeed * 4) {
                getSpinRollTimer().stop();
                _count = 0;
            }
        }
    }
    
}
