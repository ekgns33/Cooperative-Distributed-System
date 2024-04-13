import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
            "선 굵기", "색 채우기", "선 색상", "채우기 색상"
    };
    int curLineColor, curFillColor;
    IDGenerator idGenerator;
    int curID;

    public Board() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Shared Whiteboard");
        setSize(720, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buttonInit();
        boardInit();
        colorPanelInit();
        idGenerator = new IDGenerator();
    }

    private void buttonInit() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JPanel figureTypePanel = new JPanel(new GridLayout(2, 2));
        JPanel figureModifyPanel = new JPanel(new GridLayout(2, 2));

        button = new JButton[8];

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

        buttonPanel.add(figureTypePanel);
        buttonPanel.add(figureModifyPanel);

        add(buttonPanel, BorderLayout.NORTH);
    }

    private void boardInit() {
        JPanel drawingPanel = new JPanel();
        drawingPanel.setBackground(Color.WHITE);
        add(drawingPanel, BorderLayout.CENTER);
    }

    private void colorPanelInit() {
        colorPanel = new JPanel(new GridLayout(6, 2));
        colorButton = new JButton[12];
        curLineColor = curFillColor = 0;
        ColorButtonListener colorButtonListener = new ColorButtonListener();
        for (int i = 0; i < colorButton.length; i++) {
            colorButton[i] = new JButton();
            colorButton[i].setFocusPainted(false);
            colorButton[i].setBackground(colorList[i]);
            colorButton[i].addActionListener(colorButtonListener);
            colorPanel.add(colorButton[i]);
        }

        add(colorPanel, BorderLayout.WEST);
        colorPanel.setVisible(false);
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            button[curButtonIdx].setEnabled(true);
            for (int i = 0; i < 8; i++) {
                if (e.getSource() == button[i]) {
                    button[i].setEnabled(false);
                    curButtonIdx = i;
                }
            }
            if (curButtonIdx == 6 || curButtonIdx == 7) {
                colorButton[curButtonIdx == 6 ? curFillColor : curLineColor].setEnabled(true);
                colorButton[curButtonIdx == 6 ? curLineColor : curFillColor].setEnabled(false);
                colorButton[curButtonIdx == 6 ? curFillColor : curLineColor].setBorder(null);
                colorButton[curButtonIdx == 6 ? curLineColor : curFillColor].setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
                colorPanel.setVisible(true);
            } else {
                colorPanel.setVisible(false);
            }
        }

    }

    private class ColorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            colorButton[curButtonIdx == 6 ? curLineColor : curFillColor].setEnabled(true);
            colorButton[curButtonIdx == 6 ? curLineColor : curFillColor].setBorder(null);
            for (int i = 0; i < colorButton.length; i++) {
                if (e.getSource() == colorButton[i]) {
                    colorButton[i].setEnabled(false);
                    colorButton[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
                    if (curButtonIdx == 6)
                        curLineColor = i;
                    else
                        curFillColor = i;
                }
            }
        }
    }
}
