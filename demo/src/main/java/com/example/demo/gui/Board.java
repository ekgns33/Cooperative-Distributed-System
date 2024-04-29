package com.example.demo.gui;

import com.example.demo.Message;
import com.example.demo.stomp_client.EnterService;
import com.example.demo.stomp_client.StompClient;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Board extends JFrame {
    int curButtonIdx;
    JButton[] button, colorButton;
    JPanel colorPanel;
    Color[] colorList = {
            Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY,
            Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW
    };
    String[] buttonText = {
            "원", "사각형", "선", "텍스트",
            "선 굵기", "선 색상", "색 채우기"
    };
    int curColorIdx;
    IDGenerator idGenerator;
    HashMap<Integer, Figure> figureMap;
    Queue<Figure> figures;
    Figure curFigure;
    int curID, curLineWidth = 1;
    JLabel noticeLabel = new JLabel("");

    public Board(String id, String ip) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Shared Whiteboard");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                StompClient.send(Message.exitRoom(id));
            }
        });
        setLocationRelativeTo(null);

        colorPanelInit();
        buttonInit();
        boardInit();
        try {
            idGenerator = new IDGenerator(ip);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error occurred while initializing IDGenerator: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        figureMap = new HashMap<>();
        figures = new PriorityQueue<>();

        StompClient.subscribe(figureMap, figures, noticeLabel);
        StompClient.send(Message.enterRoom(id));

        EnterService enterService = new EnterService();
        try {
            for (Message message : enterService.getRoomHistories(ip)) {
                Figure curFigure = null;
                if(message.getStatus() < 3) {
                    continue;
                }
                System.out.println(message.getType() + " "+  message.getX()  + " "+ message.getY() + " "+ message.getX2() + " "+  message.getY2());
                if (message.getType() == 0) {
                    curFigure = new Circle(message.getId(), message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getFillColor(), message.getTime());
                } else if (message.getType() == 1) {
                    curFigure = new Rect(message.getId(), message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getFillColor(), message.getTime());
                } else if (message.getType() == 2) {
                    curFigure = new Line(message.getId(), message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getTime());
                } else if (message.getType() == 3) {
                    curFigure = new Text(message.getId(), message.getX(), message.getY(), message.getX2(), message.getY2(), message.getLineWidth(), message.getDrawColor(), message.getTime(), message.getText());
                }
                figureMap.put(message.getId(), curFigure);
                figures.add(curFigure);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error occurred while initializing IDGenerator: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    private void buttonInit() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        JPanel notice = new JPanel();
        JPanel panel = new JPanel();
        JPanel figureTypePanel = new JPanel(new GridLayout(2, 2));
        JPanel figureModifyPanel = new JPanel(new GridLayout(1, 3));
        JPanel figureValuePanel = new JPanel(new GridLayout(2, 1));

        button = new JButton[buttonText.length];

        ButtonListener buttonListener = new ButtonListener();
        for (int i = 0; i < button.length; i++) {
            button[i] = new JButton(buttonText[i]);
            button[i].addActionListener(buttonListener);
            button[i].setFocusPainted(false);
            if (i < 4) {
                figureTypePanel.add(button[i]);
            } else {
                figureModifyPanel.add(button[i]);
            }
        }

        curButtonIdx = 0;
        button[curButtonIdx].setEnabled(false);

        JSlider slider;
        slider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new SliderListener());

        figureValuePanel.add(colorPanel);
        figureValuePanel.add(slider);

        panel.setLayout(new BorderLayout());

        noticeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noticeLabel.setVerticalAlignment(SwingConstants.CENTER);
        notice.add(noticeLabel);

        buttonPanel.add(figureTypePanel);
        buttonPanel.add(figureModifyPanel);
        buttonPanel.add(figureValuePanel);
        panel.add(buttonPanel,BorderLayout.NORTH);
        panel.add(notice,BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);
    }

    private void boardInit() {
        JPanel drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Figure figure : figures) {
                    figure.draw(g);
                }
            }
        };
        drawingPanel.setPreferredSize(new Dimension(720, 480));
        drawingPanel.setBackground(Color.WHITE);
        add(drawingPanel, BorderLayout.CENTER);

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (curFigure != null) {
                    StompClient.send(curFigure.getMessage());
                    curFigure = null;
                }
                try {
                    if (curButtonIdx == 0) {
                        curID = idGenerator.getID();
                        curFigure = new Circle(curID, e.getX(), e.getY(), curLineWidth, curColorIdx);
                        figureMap.put(curID, curFigure);
                        figures.add(curFigure);
                    } else if (curButtonIdx == 1) {
                        curID = idGenerator.getID();
                        curFigure = new Rect(curID, e.getX(), e.getY(), curLineWidth, curColorIdx);
                        figureMap.put(curID, curFigure);
                        figures.add(curFigure);
                    } else if (curButtonIdx == 2) {
                        curID = idGenerator.getID();
                        curFigure = new Line(curID, e.getX(), e.getY(), curLineWidth, curColorIdx);
                        figureMap.put(curID, curFigure);
                        figures.add(curFigure);
                    } else if (curButtonIdx == 3) {
                        curID = idGenerator.getID();
                        curFigure = new Text(curID, e.getX(), e.getY(), curColorIdx);
                        figureMap.put(curID, curFigure);
                        figures.add(curFigure);
                    } else {
                        for (Figure figure : figures) {
                            if (figure.contains(e.getPoint())) {
                                curFigure = figure;
                            }
                        }
                        if (curFigure == null) {
                            // Do nothing
                        } else if (curButtonIdx == 4) {
                            curFigure.setLineWidth(curLineWidth);
                        } else if (curButtonIdx == 5) {
                            curFigure.setLineColor(curColorIdx);
                        } else if (curButtonIdx == 6) {
                            curFigure.setFillColor(curColorIdx);
                        }
                    }
                } catch (NullPointerException err) {
                    err.printStackTrace();
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(drawingPanel,
                            "Error occurred while initializing IDGenerator: " + err.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    err.printStackTrace();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (curButtonIdx == 3) {
                    String inputText = JOptionPane.showInputDialog("텍스트를 입력하세요:");
                    if (inputText == null) {
                        inputText = "";
                    }
                    Text textFigure = (Text) curFigure;
                    textFigure.setText(inputText);
                    textFigure.isDrawing = false;
                }

                if (curFigure != null) {
                    StompClient.send(curFigure.getMessage());
                    curFigure = null;
                }
            }
        });

        drawingPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (curButtonIdx < 4) {
                    curFigure.setEndPoint(e.getPoint());
                }
            }
        });

        Timer timer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.repaint();
                if (curFigure != null) {
                    StompClient.send(curFigure.getMessage());
                }
            }
        });
        timer.start();
    }

    private void colorPanelInit() {
        colorPanel = new JPanel(new GridLayout(2, 6));
        colorButton = new JButton[12];
        curColorIdx = 0;
        ColorButtonListener colorButtonListener = new ColorButtonListener();
        for (int i = 0; i < colorButton.length; i++) {
            colorButton[i] = new JButton();
            colorButton[i].setFocusPainted(false);
            colorButton[i].setBackground(colorList[i]);
            colorButton[i].addActionListener(colorButtonListener);
            colorPanel.add(colorButton[i]);
        }
        colorButton[curColorIdx].setEnabled(false);
        colorButton[curColorIdx].setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            button[curButtonIdx].setEnabled(true);
            for (int i = 0; i < button.length; i++) {
                if (e.getSource() == button[i]) {
                    button[i].setEnabled(false);
                    curButtonIdx = i;
                }
            }
        }
    }

    private class ColorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            colorButton[curColorIdx].setEnabled(true);
            colorButton[curColorIdx].setBorder(null);
            for (int i = 0; i < colorButton.length; i++) {
                if (e.getSource() == colorButton[i]) {
                    colorButton[i].setEnabled(false);
                    colorButton[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
                    curColorIdx = i;
                }
            }
        }
    }

    private class SliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            curLineWidth = source.getValue();
        }
    }
}
