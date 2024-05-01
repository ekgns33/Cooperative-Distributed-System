package com.example.demo.gui;

import com.example.demo.stomp_client.StompClient;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class Login extends JFrame {
    private JTextField idField;
    private JButton loginButton;
    private final String ip = "117.16.137.190";

    public Login() {
        setTitle("로그인");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        idField = new JTextField(20);
        loginButton = new JButton("로그인");

        loginButton.addActionListener(e -> {
            if (Login.this.authenticate(ip)) {
                Board board = new Board(idField.getText(), ip);
                board.setVisible(true);
                Login.this.dispose();
            } else {
                JOptionPane.showMessageDialog(Login.this, "로그인 실패");
            }
        });

        add(new JLabel("닉네임"));
        add(idField);
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
