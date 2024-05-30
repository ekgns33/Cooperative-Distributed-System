package com.example.demo;

import com.example.demo.gui.Login;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class DemoApplication {

    private static final String serverIp = "localhost";
    private static final int serverPort = 8080;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login(serverIp, serverPort);
            login.setVisible(true);
        });
        SpringApplication.run(DemoApplication.class, args);
    }

}
