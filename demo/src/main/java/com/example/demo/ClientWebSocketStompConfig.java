package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ClientWebSocketStompConfig {
	public WebSocketStompClient WebSocketStompClient(WebSocketStompClient webSocketClient,
		StompSessionHandler stompSessionHandler) {

		// client to server message converter
		webSocketClient.setMessageConverter(new MappingJackson2MessageConverter());

		StompHeaders stompHeaders = new StompHeaders();
		stompHeaders.add("host", "karim");

		Object[] urlVariables = {};
		String url = "ws://localhost:8080/whiteboard";
		webSocketClient.connect(url, null, stompHeaders, stompSessionHandler, urlVariables);

		return webSocketClient;
	}

	public WebSocketStompClient webSocketClient() {
		WebSocketClient webSocketClient = new StandardWebSocketClient();
		return new WebSocketStompClient(webSocketClient);
	}
	public StompSessionHandler stompSessionHandler() {
		return new ClientWebSocketStompSessionHandler();
	}
}
