import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JFrame {
    int curButtonIdx;
    JButton[] button;

    public Board() {
        setTitle("Shared Whiteboard");
        setSize(720, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buttonInit();
        boardInit();
    }

    private void buttonInit() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JPanel figureTypePanel = new JPanel(new GridLayout(2, 2));
        JPanel figureModifyPanel = new JPanel(new GridLayout(2, 2));

        button = new JButton[8];

        button[0] = new JButton("원");
        button[1] = new JButton("사각형");
        button[2] = new JButton("선");
        button[3] = new JButton("텍스트");
        button[4] = new JButton("선 굵기");
        button[5] = new JButton("색 채우기");
        button[6] = new JButton("선 색상");
        button[7] = new JButton("채우기 색상");

        ButtonListener buttonListener = new ButtonListener();
        for (int i = 0; i < button.length; i++) {
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

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            button[curButtonIdx].setEnabled(true);
            for (int i = 0; i < 8; i++) {
                if ((JButton) e.getSource() == button[i]) {
                    button[i].setEnabled(false);
                    curButtonIdx = i;
                }
            }

        }
    }
}
