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
 * 
 * @author cw.ahbong
 * @author NTUCS-MSLAB
 */
public class RollCallUi extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private static final int text_area_width = 110;
    private static final int text_area_height = 360;
    
    private RollCallFileSet _fileset = new RollCallFileSet(new File("student.txt"));
    //private int _luckyManNumber = -1;
    private String _luckyManName;
    private int _rollSpeed = 60;
    private RollCaller _rollCaller;

    /*
     * Panel(s)
     */
    private JPanel _centerPanel;
    private JPanel _eastPanel;
    private JPanel _southPanel;

    public JPanel getCenterPanel() {
        if(_centerPanel==null) {
            _centerPanel = new JPanel(){
                private static final long serialVersionUID = 1L;
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(new Color(205,127,50));
                    int width = 110;
                    int height = 260;
                    for(int i=0; i<3; i++) {
                        g.fillRoundRect(35+i*(width+10),60,width-10,height-10,10,10);
                        g.drawRoundRect(30+i*(width+10),55,width,height,10,10);
                        add(getRollTextArea(i));
                    }
                }
            };
            getSpinRollTimer();
            _centerPanel.setBackground(Color.black);
            _centerPanel.setLayout(null);
        }
        return _centerPanel;
    }

    public JPanel getEastPanel() {
        if(_eastPanel==null) {
            _eastPanel = new JPanel();
            _eastPanel.setLayout(new BorderLayout());
            _eastPanel.add(getPlayButton(), BorderLayout.CENTER);
        }
        return _eastPanel;
    }

    public JPanel getSouthPanel() {
        if(_southPanel==null) {
            _southPanel = new JPanel();
            _southPanel.setBackground(new Color(220,236,220));
            _southPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
            _southPanel.add(getLogoLabel());
            _southPanel.add(getSpeedSlider());
            _southPanel.add(getAbsentButton());
            _southPanel.add(getAboutButton());
            _southPanel.add(getInfoLabel());
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
            _rollTextArea[order].setBounds(35+order*(text_area_width+10),0,text_area_width-10,text_area_height);
            // DFKai-sb is 標楷體, English font name is faster.
            _rollTextArea[order].setFont(new Font("DFKai-sb",Font.PLAIN,100)); 
            _rollTextArea[order].setForeground(Color.black);
            _rollTextArea[order].setOpaque(false);
            _rollTextArea[order].setFocusable(false);
            //_rollTextArea[order].setText(_text.get(2) +"\n"+ _text.get(1) +"\n"+ _text.get(0));
        }
        return _rollTextArea[order];
    }
    
    public Timer getSpinRollTimer() {
        if(_spinRollTimer==null) {
            _spinRollTimer = new Timer(100, null);
            _spinRollTimer.setInitialDelay(0);
            _spinRollTimer.addActionListener(new RollTask(0));
            _spinRollTimer.addActionListener(new RollTask(1));
            _spinRollTimer.addActionListener(new RollTask(2));
        }
        return _spinRollTimer;
    }
    
    /*
     * Component(s) in east panel.
     */
    private JButton _playButton;
    
    public JButton getPlayButton() {
        if(_playButton==null) {
            _playButton = new JButton(new ImageIcon("play.png"));
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
    private JLabel _infoLabel;
    private JLabel _logoLabel;
    
    public JButton getAboutButton() {
        if(_aboutButton==null) {
            _aboutButton = new BottomButton("About");
            _aboutButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null,
                            "By NTUCS-MSLAB, modified by cw.ahbong.",
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
            _speedSlider = new JSlider(SwingConstants.HORIZONTAL, 30, 90, 60);
            _speedSlider.setMajorTickSpacing(30);
            _speedSlider.setPaintTicks(true);
            _speedSlider.setSnapToTicks(true);
            Dictionary<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
            labels.put(30, new JLabel("Slow"));
            labels.put(90, new JLabel("Fast"));
            _speedSlider.setLabelTable(labels);
            _speedSlider.setPaintLabels(true);
            _speedSlider.setOpaque(false);
            _speedSlider.setPreferredSize(new Dimension(100,50));
            _speedSlider.addChangeListener(new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent arg0) {
                    _rollSpeed = _speedSlider.getValue();
                }
            });
        }
        return _speedSlider;
    }

    public JLabel getInfoLabel() {
        if(_infoLabel==null) {
            _infoLabel = new JLabel("Add successfully!");
            _infoLabel.setFont(new Font("Comic Sans MS",Font.PLAIN,20));
            _infoLabel.setFocusable(false);
            _infoLabel.setBackground(new Color(220,236,220));
            _infoLabel.setForeground(new Color(220,236,220));
        }
        return _infoLabel;
    }

    public JLabel getLogoLabel() {
        if(_logoLabel==null) {
            _logoLabel = new JLabel("NTUCS-MSLAB");
            _logoLabel.setFont(new Font("Arial",Font.ITALIC,14));
            _logoLabel.setFocusable(false);
            _logoLabel.setOpaque(false);
            _logoLabel.setForeground(Color.BLACK);
        }
        return _logoLabel;
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
        System.err.println("Inited");
    }
    
    private class RollTask implements ActionListener {
        
        private static final int _rollSpeed = 30;
        
        private int _order;
        private int _count;
        private List<String> _text = new ArrayList<String>();
        private Random _random = new Random();
        
        public RollTask(int order) {
            _order = order;
            _count = 0;
            if(getRollCaller().getTotalStudentNum()>2) {
                for(int i=0; i<3; i++) {
                    _text.add(
                            getRollCaller().getNameList().get(_random.nextInt(
                                    getRollCaller().getNameList().size()))
                            .substring(_order, _order+1));
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
        
        @Override
        public void actionPerformed(ActionEvent e) {
            int left = (int)getRollTextArea(_order).getLocation().getX();
            int top = (int)getRollTextArea(_order).getLocation().getY();
            ++_count;
            if(_count <= _rollSpeed * _order)
                getRollTextArea(_order).setLocation(left, top+20);
            else if(_count <= _rollSpeed * (_order+1))
                getRollTextArea(_order).setLocation(left, top+10);
            else if(_count <= _rollSpeed * (_order+2))
                getRollTextArea(_order).setLocation(left, top+2);
            
            if(_count == _rollSpeed * 5) {
                getSpinRollTimer().stop();
                _count = 0;
            }
            
            if(getRollTextArea(_order).getLocation().getY() == 60) {
                if(_count == _rollSpeed * (_order+1) - 360/_rollSpeed)
                    _text.add(_luckyManName.substring(_order,_order+1));
                else
                    _text.add(getRollCaller().getNameList().get(_random.nextInt(getRollCaller().getNameList().size())).substring(_order,_order+1));
                _text.remove(0);
                getRollTextArea(_order).setLocation(left,-60);
                getRollTextArea(_order).setText(_text.get(2)+"\n"+_text.get(1)+"\n"+_text.get(0));
            }
        }
    }
    
}
