package com.example.demo;

import javax.swing.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.example.demo.gui.Board;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			Board board = new Board();
			board.setVisible(true);
		});






		SpringApplication.run(DemoApplication.class, args);
	}

}
