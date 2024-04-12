import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JFrame {
    private JPanel buttonPanel, drawingPanel;

    public Board() {
        setTitle("Shared Whiteboard");
        setSize(720, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buttonInit();
        boardInit();
    }

    private void buttonInit() {
        buttonPanel = new JPanel(new GridLayout(1, 2));
        JPanel figureTypePanel = new JPanel(new GridLayout(2, 2));
        JPanel figureModifyPanel = new JPanel(new GridLayout(2, 2));

        JButton[] button = new JButton[8];

        button[0] = new JButton("원");
        button[1] = new JButton("사각형");
        button[2] = new JButton("선");
        button[3] = new JButton("텍스트");
        button[4] = new JButton("선 굵기");
        button[5] = new JButton("색 채우기");
        button[6] = new JButton("선 색상");
        button[7] = new JButton("채우기 색상");

        for (int i = 0; i < 4; i++ )
            figureTypePanel.add(button[i]);
        for (int i = 4; i < 8; i++ )
            figureModifyPanel.add(button[i]);
        buttonPanel.add(figureTypePanel);
        buttonPanel.add(figureModifyPanel);

        add(buttonPanel, BorderLayout.NORTH);
    }

    private void boardInit(){
        drawingPanel = new JPanel();
        drawingPanel.setBackground(Color.WHITE);
        add(drawingPanel, BorderLayout.CENTER);
    }
}
