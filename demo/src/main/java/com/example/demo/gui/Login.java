package com.example.demo.gui;

import com.example.demo.stomp_client.StompClient;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class Login extends JFrame {
    private JTextField idField;
    private JTextField ipField;
    private JButton loginButton;

    public Login() throws ExecutionException, InterruptedException {
        setTitle("로그인");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        idField = new JTextField(20);
        ipField = new JTextField(20);
        loginButton = new JButton("로그인");

        loginButton.addActionListener(e -> {
            String id = idField.getText();
            if (Login.this.authenticate()) {
                Board board = new Board();
                board.setVisible(true);
                Login.this.dispose();
            }
            else {
                JOptionPane.showMessageDialog(Login.this, "error");
            }
        });

        add(new JLabel("ID:"));
        add(idField);
        add(new JLabel("IP:"));
        add(ipField);
        add(loginButton);

        String id = idField.getText();
        String ip = ipField.getText();
        StompClient.connect(ip, id);

        setLocationRelativeTo(null);
    }

    private boolean authenticate() {
        return true;
    }
}
