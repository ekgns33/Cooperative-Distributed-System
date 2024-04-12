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
        buttonPanel = new JPanel(new GridLayout(2, 2));
        JButton[] buttons = new JButton[4];

        buttons[0] = new JButton("button 0");
        buttons[1] = new JButton("button 1");
        buttons[2] = new JButton("button 2");
        buttons[3] = new JButton("button 3");

        for (JButton button : buttons)
            buttonPanel.add(button);

        add(buttonPanel, BorderLayout.NORTH);
    }

    private void boardInit(){
        drawingPanel = new JPanel();
        drawingPanel.setBackground(Color.WHITE);
        add(drawingPanel, BorderLayout.CENTER);
    }
}
