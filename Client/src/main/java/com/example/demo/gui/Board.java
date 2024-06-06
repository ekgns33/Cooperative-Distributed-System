package com.example.demo.gui;

import com.example.demo.Message;
import com.example.demo.stomp_client.EnterService;
import com.example.demo.stomp_client.StompClient;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Board extends JFrame {
    ToolButton[] button, lineWidthButton;
    ColorButton[] colorButton;
    JPanel buttonPanel;
    Color[] colorList = {
            Color.BLACK, Color.GRAY,
            Color.BLUE, Color.CYAN,
            Color.GREEN, Color.YELLOW,
            Color.ORANGE, Color.PINK,
            Color.MAGENTA, Color.RED
    };
    String[] buttonText = {
            "원", "사각형", "선", "텍스트",
            "선 굵기", "선 색상", "색 채우기",
            "저장", "불러오기"
    };
    int curButtonIdx = 0;
    int curColorIdx = 0;
    IDGenerator idGenerator;
    ConcurrentMap<Integer, Figure> figureMap;
    PriorityQueue<Figure> figures;
    Figure curFigure;
    int curID, curLineWidth = 1;
    JLabel noticeLabel = new JLabel("");
    private final Object lock = new Object();
    BooleanWrapper lockResult = new BooleanWrapper(false);

    public Board(String id, String ip, int port) {
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

        buttonInit();
        boardInit();
        try {
            idGenerator = new IDGenerator(ip, port);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error occurred while initializing IDGenerator: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        figureMap = new ConcurrentHashMap<>();
        figures = new PriorityQueue<>();

        StompClient.subscribe(figureMap, figures, noticeLabel, lock, lockResult);
        StompClient.send(Message.enterRoom(id));

        EnterService enterService = new EnterService();
        try {
            for (Message message : enterService.getRoomHistories(ip, port)) {
                Figure curFigure = null;
                if (message.getStatus() < 3) {
                    continue;
                }
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
        buttonPanel = new JPanel();
        JPanel notice = new JPanel();
        JPanel panel = new JPanel();

        Dimension size = new Dimension(40, 40);

        ButtonListener buttonListener = new ButtonListener();
        SaveButtonListener saveButtonListener = new SaveButtonListener();
        LoadButtonListener loadButtonListener = new LoadButtonListener();
        LineWidthButtonListener lineWidthButtonListener = new LineWidthButtonListener();
        ColorButtonListener colorButtonListener = new ColorButtonListener();

        button = new ToolButton[buttonText.length];
        for (int i = 0; i < button.length; i++) {
            button[i] = new ToolButton(i, size);
            if (i < 7) {
                button[i].addActionListener(buttonListener);
            }
            buttonPanel.add(button[i]);
        }
        button[7].addActionListener(saveButtonListener);
        button[8].addActionListener(loadButtonListener);

        // 구분선
        JPanel line = new JPanel();
        line.setBackground(new Color(0xE6E6E6));
        line.setPreferredSize(new Dimension(1, 42));
        buttonPanel.add(line);

        // 선 굵기 버튼
        lineWidthButton = new ToolButton[6];
        for (int i = 1; i <= 5; i++) {
            lineWidthButton[i] = new ToolButton(2, size);
            lineWidthButton[i].setLineWidth(i);
            lineWidthButton[i].addActionListener(lineWidthButtonListener);
            buttonPanel.add(lineWidthButton[i]);
        }

        // 구분선
        line = new JPanel();
        line.setBackground(new Color(0xE6E6E6));
        line.setPreferredSize(new Dimension(1, 42));
        buttonPanel.add(line);

        size = new Dimension(32, 32);

        // 색상 버튼
        colorButton = new ColorButton[colorList.length];
        for (int i = 0; i < colorList.length; i++) {
            colorButton[i] = new ColorButton(colorList[i]);
            colorButton[i].setPreferredSize(size);
            colorButton[i].setMaximumSize(size);
            colorButton[i].setMinimumSize(size);
            colorButton[i].addActionListener(colorButtonListener);
            buttonPanel.add(colorButton[i]);
        }

        button[curButtonIdx].toggle();
        lineWidthButton[curLineWidth].toggle();
        colorButton[curColorIdx].toggle();

        panel.setLayout(new BorderLayout());
        buttonPanel.setBackground(new Color(0xF5F5F5));
        Border bottomBorder = new MatteBorder(0, 0, 1, 0, new Color(0xE6E6E6));
        buttonPanel.setBorder(bottomBorder);

        noticeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noticeLabel.setVerticalAlignment(SwingConstants.CENTER);
        notice.add(noticeLabel);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(notice, BorderLayout.CENTER);
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
                        StompClient.send(Message.tryLock(curID));
                        curFigure = new Circle(curID, e.getX(), e.getY(), curLineWidth, curColorIdx);
                        figureMap.put(curID, curFigure);
                        figures.add(curFigure);
                    } else if (curButtonIdx == 1) {
                        curID = idGenerator.getID();
                        StompClient.send(Message.tryLock(curID));
                        curFigure = new Rect(curID, e.getX(), e.getY(), curLineWidth, curColorIdx);
                        figureMap.put(curID, curFigure);
                        figures.add(curFigure);
                    } else if (curButtonIdx == 2) {
                        curID = idGenerator.getID();
                        StompClient.send(Message.tryLock(curID));
                        curFigure = new Line(curID, e.getX(), e.getY(), curLineWidth, curColorIdx);
                        figureMap.put(curID, curFigure);
                        figures.add(curFigure);
                    } else if (curButtonIdx == 3) {
                        curID = idGenerator.getID();
                        StompClient.send(Message.tryLock(curID));
                        curFigure = new Text(curID, e.getX(), e.getY(), curColorIdx);
                        figureMap.put(curID, curFigure);
                        figures.add(curFigure);
                    } else {
                        Figure selectedFigure = null;
                        for (Figure figure : figures) {
                            if (figure.contains(e.getPoint())) {
                                selectedFigure = figure;
                            }
                        }
                        if (selectedFigure == null) {
                            return;
                        }
                        synchronized (lock) {
                            StompClient.send(Message.tryLock(selectedFigure.getID()));
                            lock.wait();
                        }
                        if (!lockResult.isValue()) {
                            return;
                        }
                        if (curButtonIdx == 4) {
                            selectedFigure.setLineWidth(curLineWidth);
                        } else if (curButtonIdx == 5) {
                            selectedFigure.setLineColor(curColorIdx);
                        } else if (curButtonIdx == 6) {
                            selectedFigure.setFillColor(curColorIdx);
                        }
                        StompClient.send(selectedFigure.getMessage());
                        StompClient.send(Message.unlock(selectedFigure.getID()));
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
                }

                if (curFigure != null) {
                    StompClient.send(curFigure.getMessage());
                    StompClient.send(Message.unlock(curFigure.getID()));
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

    private void save() {
        PriorityQueue<Figure> capture = new PriorityQueue<>(figures);
        String filePath = "save.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            while (!capture.isEmpty()) {
                String element = capture.poll().getInfo();
                writer.write(element);
                if (!capture.isEmpty())
                    writer.newLine();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("[Log] Save complete");
    }

    private void load() {
        String filePath = "save.txt";
        Path path = Paths.get(filePath);

        if (Files.exists(path)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            System.out.println("[Log] Load complete");
        } else {
            System.out.println("[Log] File does not exist");
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            save();
        }
    }

    private class LoadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            load();
        }
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int next = 0;
            for (int i = 0; i < button.length; i++) {
                if (e.getSource() == button[i]) {
                    next = i;
                    break;
                }
            }
            if (next != curButtonIdx) {
                button[curButtonIdx].toggle();
                button[next].toggle();
                curButtonIdx = next;
            }
        }
    }

    private class LineWidthButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int next = 0;
            for (int i = 1; i <= 5; i++) {
                if (e.getSource() == lineWidthButton[i]) {
                    next = i;
                    break;
                }
            }
            if (next != curLineWidth) {
                lineWidthButton[curLineWidth].toggle();
                lineWidthButton[next].toggle();
                curLineWidth = next;
            }
        }
    }

    private class ColorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int next = 0;
            for (int i = 0; i < colorButton.length; i++) {
                if (e.getSource() == colorButton[i]) {
                    next = i;
                    break;
                }
            }
            if (next != curColorIdx) {
                colorButton[curColorIdx].toggle();
                colorButton[next].toggle();
                curColorIdx = next;
                button[5].setColor(colorList[curColorIdx]);
            }
        }
    }
}
