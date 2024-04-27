import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

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
    int curID, curStrokeWidth = 2;

    public Board() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Shared Whiteboard");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        colorPanelInit();
        buttonInit();
        boardInit();
        idGenerator = new IDGenerator();
        figureMap = new HashMap<>();
        figures = new PriorityQueue<>();
    }

    private void buttonInit() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JPanel figurePanel = new JPanel(new GridLayout(1, 2));
        JPanel figureTypePanel = new JPanel(new GridLayout(2, 2));
        JPanel figureModifyPanel = new JPanel(new GridLayout(1, 3));

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

        figurePanel.add(figureTypePanel);
        figurePanel.add(figureModifyPanel);

        buttonPanel.add(figurePanel);
        buttonPanel.add(colorPanel);

        add(buttonPanel, BorderLayout.NORTH);
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
                try {
                    if (curButtonIdx == 0) {
                        curFigure = new Circle(e.getX(), e.getY(), curStrokeWidth, colorList[curColorIdx]);
                        figureMap.put(idGenerator.getID(), curFigure);
                        figures.add(curFigure);
                    } else if (curButtonIdx == 1) {
                        curFigure = new Rect(e.getX(), e.getY(), curStrokeWidth, colorList[curColorIdx]);
                        figureMap.put(idGenerator.getID(), curFigure);
                        figures.add(curFigure);
                    } else if (curButtonIdx == 2) {
                        curFigure = new Line(e.getX(), e.getY(), curStrokeWidth, colorList[curColorIdx]);
                        figureMap.put(idGenerator.getID(), curFigure);
                        figures.add(curFigure);
                    } else if (curButtonIdx == 3) {
                        curFigure = new Text(e.getX(), e.getY(), colorList[curColorIdx]);
                        figureMap.put(idGenerator.getID(), curFigure);
                        figures.add(curFigure);
                    } else {
                        curFigure = null;
                        for (Figure figure : figures) {
                            if (figure.contains(e.getPoint())) {
                                curFigure = figure;
                            }
                        }
                        if (curFigure == null) {
                            // Do nothing
                        } else if (curButtonIdx == 4) {
                            // Line width
                        } else if (curButtonIdx == 5) {
                            curFigure.setLineColor(colorList[curColorIdx]);
                        } else if (curButtonIdx == 6) {
                            curFigure.setFillColor(colorList[curColorIdx]);
                        }
                        curFigure = null;
                    }
                } catch (NullPointerException err) {
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
                if (curButtonIdx < 4) {
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
}
