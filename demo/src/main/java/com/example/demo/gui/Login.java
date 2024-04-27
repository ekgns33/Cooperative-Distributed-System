package com.example.demo.gui;

import com.example.demo.stomp_client.StompClient;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class Login extends JFrame {
    private JTextField idField;
    private JTextField ipField;
    private JButton loginButton;

    public Login() {
        setTitle("로그인");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        idField = new JTextField(20);
        ipField = new JTextField(20);
        loginButton = new JButton("로그인");

        loginButton.addActionListener(e -> {
            String id = idField.getText();
            if (Login.this.authenticate(ipField.getText())) {
                Board board = new Board(idField.getText(), ipField.getText());
                board.setVisible(true);
                Login.this.dispose();
            } else {
                JOptionPane.showMessageDialog(Login.this, "로그인 실패");
            }
        });

        add(new JLabel("ID:"));
        add(idField);
        add(new JLabel("IP:"));
        add(ipField);
        add(loginButton);

        setLocationRelativeTo(null);
    }

    private boolean authenticate(String ip) {
        try {
            StompClient.connect(ip);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
