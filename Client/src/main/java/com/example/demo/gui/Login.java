package com.example.demo.gui;

import com.example.demo.stomp_client.StompClient;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {
    private JTextField idField;
    private JButton loginButton;

    public Login(String ip, int port) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("로그인");
        setSize(300, 160);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        idField = new JTextField(20);
        loginButton = new JButton("로그인");
        Dimension buttonSize = new Dimension(280, 30);
        loginButton.setPreferredSize(buttonSize);
        loginButton.setMaximumSize(buttonSize);

        loginButton.addActionListener(e -> {
            if (Login.this.authenticate(ip, port)) {
                Board board = new Board(idField.getText(), ip, port);
                board.setVisible(true);
                Login.this.dispose();
            }
            else {
                JOptionPane.showMessageDialog(Login.this, "로그인 실패");
            }
        });

        add(Box.createVerticalStrut(10));
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        labelPanel.add(new JLabel("닉네임"));
        add(labelPanel);
        add(Box.createVerticalStrut(8));

        JPanel idFieldPanel = new JPanel();
        idFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        idFieldPanel.add(idField);
        add(idFieldPanel);

        add(Box.createVerticalStrut(8));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(loginButton);
        add(buttonPanel);

        add(Box.createVerticalStrut(10));

        setLocationRelativeTo(null);
    }

    private boolean authenticate(String ip, int port) {
        try {
            StompClient.connect(ip, port);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
